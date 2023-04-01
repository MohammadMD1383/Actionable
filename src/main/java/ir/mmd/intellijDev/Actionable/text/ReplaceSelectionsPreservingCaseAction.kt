package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.showInputDialog

class ReplaceSelectionsPreservingCaseAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		val text = showInputDialog(project, "Replace Selections Preserving Case")
		if (text.isNullOrBlank()) {
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
