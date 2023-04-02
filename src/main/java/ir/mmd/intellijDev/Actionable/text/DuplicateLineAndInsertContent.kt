package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.caretsAndSelectionsAreOnTheSameLine
import ir.mmd.intellijDev.Actionable.text.settings.SettingsState
import ir.mmd.intellijDev.Actionable.ui.showMultilineInputDialog
import ir.mmd.intellijDev.Actionable.util.ext.addCaret
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.replaceRanges
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

open class DuplicateLineAndInsertContent : ActionBase(), DumbAware {
	context (LazyEventContext)
	protected open fun getReplacements() = showMultilineInputDialog(
		project,
		"Duplicate Line And Insert Content",
		10,
		50
	)?.split('\n')?.toMutableList()
	
	context (LazyEventContext)
	override fun performAction() {
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
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class DuplicateLineAndPasteClipboardContentAction : DuplicateLineAndInsertContent() {
	context(LazyEventContext)
	override fun getReplacements(): MutableList<String>? {
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String)
			.split("\n").toMutableList()
		
		return contents.ifEmpty { null }
	}
}
