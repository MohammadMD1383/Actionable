package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.TextRange
import com.intellij.ui.components.JBTextArea
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.*

class DuplicateLineAndInsertContent : AnAction() {
	context (LazyEventContext)
	private fun caretsAreAtTheSameLine(): Boolean {
		val distinctLines = allCarets.map { it.logicalPosition.line }.distinct()
		if (distinctLines.size != 1) {
			return false
		}
		
		val line = distinctLines.first()
		return !(document.getLineNumber(allCarets.first().selectionStart) != line ||
			document.getLineNumber(allCarets.last().selectionEnd) != line)
	}
	
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		if (!caretsAreAtTheSameLine()) {
			return
		}
		
		val textArea = JBTextArea(5, 30)
		
		val result = DialogBuilder(project).apply {
			setCenterPanel(textArea)
			setTitle("Duplicate Line And Insert Content")
			removeAllActions()
			addOkAction()
			addCancelAction()
		}.show()
		
		if (result != DialogWrapper.OK_EXIT_CODE || textArea.text.isBlank()) {
			return
		}
		
		val lineNumber = document.getLineNumber(allCarets.first().offset)
		var lineStartOffset = document.getLineStartOffset(lineNumber)
		val lineEndOffset = document.getLineEndOffset(lineNumber)
		val rawLine = document.getText(TextRange(lineStartOffset, lineEndOffset))
		val replacements = textArea.text.split('\n').toMutableList()
		val replacementRanges = allCarets.map {
			it.selectionStart - lineStartOffset..it.selectionEnd - lineStartOffset
		}
		
		project.runWriteCommandAction {
			replacements.removeFirst().let {
				val (newLine, ranges) = rawLine.replaceRanges(replacementRanges, it)
				
				document.replaceString(lineStartOffset, lineEndOffset, newLine)
				
				allCarets.forEachIndexed { i, caret ->
					val range = ranges[i]
					caret moveTo lineStartOffset + range.last
					caret.setSelection(lineStartOffset + range.first, lineStartOffset + range.last)
				}
				
				lineStartOffset += newLine.length
			}
			
			replacements.forEach {
				val (newLine, ranges) = rawLine.replaceRanges(replacementRanges, it)
				
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
}
