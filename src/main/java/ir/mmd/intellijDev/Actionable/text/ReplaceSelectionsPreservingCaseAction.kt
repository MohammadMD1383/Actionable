package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.*

class ReplaceSelectionsPreservingCaseAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		val textField = JBTextField()
		
		val result = DialogBuilder(editor.contentComponent).apply {
			setTitle("Replace Selections Preserving Case")
			setCenterPanel(textField)
			removeAllActions()
			addOkAction()
			addCancelAction()
		}.show()
		
		val text = textField.text
		if (result == DialogWrapper.CANCEL_EXIT_CODE || text.isBlank()) {
			return
		}
		
		runWriteCommandAction {
			allCarets.withEach {
				replaceSelectedText { text toCaseStyleOf it }
			}
		}
	}
	
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor and allCarets.haveSelection }
	override fun isDumbAware() = true
}
