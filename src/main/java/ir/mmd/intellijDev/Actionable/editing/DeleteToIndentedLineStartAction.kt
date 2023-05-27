package ir.mmd.intellijDev.Actionable.editing

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.getLineStartIndentLength

class DeleteToIndentedLineStartAction : MultiCaretAction(), DumbAware {
	context (LazyEventContext)
	override fun perform() {
		val lineNumber = document.getLineNumber(caret.offset)
		val lineStartOffset = document.getLineStartOffset(lineNumber)
		val lineEndOffset = document.getLineEndOffset(lineNumber)
		val lineIndentEndOffset = lineStartOffset + document.getLineStartIndentLength(lineNumber)
		val line = document.getText(TextRange(lineStartOffset, lineEndOffset))
		
		runWriteCommandAction {
			when {
				line.isBlank() -> {
					document.deleteString(lineStartOffset, lineEndOffset)
				}
				
				caret.offset <= lineIndentEndOffset -> {
					document.deleteString(lineStartOffset, caret.offset)
				}
				
				caret.offset > lineIndentEndOffset -> {
					document.deleteString(lineIndentEndOffset, caret.offset)
				}
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}
