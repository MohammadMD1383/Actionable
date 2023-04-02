package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.caretsAndSelectionsAreOnTheSameLine
import ir.mmd.intellijDev.Actionable.text.settings.SettingsState
import ir.mmd.intellijDev.Actionable.ui.showMultilineInputDialog
import ir.mmd.intellijDev.Actionable.util.ext.addCaret
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.replaceRanges
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

open class DuplicateLineAndInsertContent : AnAction() {
	context (LazyEventContext)
	protected open fun getReplacements() = showMultilineInputDialog(
		project,
		"Duplicate Line And Insert Content",
		10,
		50
	)?.split('\n')?.toMutableList()
	
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		if (!caretsAndSelectionsAreOnTheSameLine()) {
			return
		}
		
		val replacements = getReplacements() ?: return
		val preserveCase = service<SettingsState>().preserveCase
		val lineNumber = document.getLineNumber(allCarets.first().offset)
		var lineStartOffset = document.getLineStartOffset(lineNumber)
		val lineEndOffset = document.getLineEndOffset(lineNumber)
		val rawLine = document.getText(TextRange(lineStartOffset, lineEndOffset))
		val replacementRanges = allCarets.map {
			it.selectionStart - lineStartOffset..it.selectionEnd - lineStartOffset
		}
		
		runWriteCommandAction {
			replacements.removeFirst().let {
				val (newLine, ranges) = rawLine.replaceRanges(replacementRanges, it, preserveCase)
				
				document.replaceString(lineStartOffset, lineEndOffset, newLine)
				
				allCarets.forEachIndexed { i, caret ->
					val range = ranges[i]
					caret moveTo lineStartOffset + range.last
					caret.setSelection(lineStartOffset + range.first, lineStartOffset + range.last)
				}
				
				lineStartOffset += newLine.length
			}
			
			replacements.forEach {
				val (newLine, ranges) = rawLine.replaceRanges(replacementRanges, it, preserveCase)
				
				document.insertString(lineStartOffset++, "\n$newLine")
				
				ranges.forEach { range ->
					caretModel.addCaret(lineStartOffset + range.last)
						?.setSelection(lineStartOffset + range.first, lineStartOffset + range.last)
				}
				
				lineStartOffset += newLine.length
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun isDumbAware() = true
}

class DuplicateLineAndPasteClipboardContentAction : DuplicateLineAndInsertContent() {
	context(LazyEventContext)
	override fun getReplacements(): MutableList<String>? {
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String)
			.split("\n").toMutableList()
		
		return contents.ifEmpty { null }
	}
}
