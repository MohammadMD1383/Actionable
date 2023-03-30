package ir.mmd.intellijDev.Actionable.editing

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.enableIf

abstract class AddLineAction(private val above: Boolean) : MultiCaretAction() {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val lineNumber = document.getLineNumber(caret.offset)
		
		runWriteCommandAction {
			document.insertString(
				if (above) document.getLineStartOffset(lineNumber) else document.getLineEndOffset(lineNumber),
				"\n"
			)
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

class AddLineAboveCaretWMAction : AddLineAction(true)

class AddLineBelowCaretWMAction : AddLineAction(false)
