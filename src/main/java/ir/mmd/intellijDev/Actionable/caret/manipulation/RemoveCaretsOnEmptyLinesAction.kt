package ir.mmd.intellijDev.Actionable.caret.manipulation

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.internal.doc.Documentation
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.getText

@Documentation(
	title = "Remove Carets On Empty Lines",
	description = "Removes all carets that are on empty/blank lines.",
	example = """Everything is clear"""
)
class RemoveCaretsOnEmptyLinesAction : MultiCaretAction() {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val lineNumber = document.getLineNumber(caret.offset)
		val startOffset = document.getLineStartOffset(lineNumber)
		val endOffset = document.getLineEndOffset(lineNumber)
		val line = document.getText(startOffset..endOffset)
		
		if (line.isBlank()) {
			caretModel.removeCaret(caret)
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor && caretModel.caretCount > 1 }
	override fun isDumbAware() = true
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
