package ir.mmd.intellijDev.Actionable.fix.rider

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_ENTER
import ir.mmd.intellijDev.Actionable.action.action
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*

@Keep
class StartLineUp : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		val enterAction = action(ACTION_EDITOR_ENTER)!!
		
		editor.caretModel.allCarets.asReversed().forEach { caret ->
			caret.removeSelection()
			val line = caret.logicalPosition.line
			
			if (line == 0) {
				e.project.runWriteCommandAction { document.insertString(0, "\n") }
				caret moveTo 0
			} else {
				caret moveTo document.getLineEndOffset(line - 1)
				enterAction.actionPerformed(e)
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
