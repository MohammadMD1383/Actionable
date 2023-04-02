package ir.mmd.intellijDev.Actionable.caret.manipulation

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction

class RemoveCaretsOnEmptyLinesAction : MultiCaretAction(), DumbAware {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val lineNumber = document.getLineNumber(caret.offset)
		val startOffset = document.getLineStartOffset(lineNumber)
		val endOffset = document.getLineEndOffset(lineNumber)
		val line = document.getText(TextRange(startOffset, endOffset))
		
		if (line.isBlank()) {
			caretModel.removeCaret(caret)
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && caretCount > 1
}
