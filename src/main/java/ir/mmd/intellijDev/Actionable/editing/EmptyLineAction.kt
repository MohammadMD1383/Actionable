package ir.mmd.intellijDev.Actionable.editing

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.getLineStartIndentLength

open class EmptyLineAction : MultiCaretAction(), DumbAware {
	context (LazyEventContext)
	open fun getTextRange(lineNumber: Int) = document.run { getLineStartOffset(lineNumber)..getLineEndOffset(lineNumber) }
	
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val lineNumber = document.getLineNumber(caret.offset)
		val range = getTextRange(lineNumber)
		
		runWriteCommandAction {
			document.deleteString(range.first, range.last)
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class IndentPreservingEmptyLineAction : EmptyLineAction() {
	context(LazyEventContext)
	override fun getTextRange(lineNumber: Int): IntRange {
		val lineStartOffset = document.getLineStartOffset(lineNumber)
		val lineEndOffset = document.getLineEndOffset(lineNumber)
		val lineIndentEndOffset = lineStartOffset + document.getLineStartIndentLength(lineNumber)
		val line = document.getText(TextRange(lineStartOffset, lineEndOffset))
		
		return (if (line.isBlank()) lineStartOffset else lineIndentEndOffset)..lineEndOffset
	}
}
