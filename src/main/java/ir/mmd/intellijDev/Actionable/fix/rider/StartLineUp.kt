package ir.mmd.intellijDev.Actionable.fix.rider

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_ENTER
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.action
import ir.mmd.intellijDev.Actionable.util.ext.*

class StartLineUp : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		val enterAction = action(ACTION_EDITOR_ENTER)
		
		editor.caretModel.allCarets.asReversed().forEach { caret ->
			val line = caret.logicalPosition.line - 1
			val upperLineEnd = document.getLineEndOffset(line)
			
			caret.removeSelection()
			caret moveTo upperLineEnd
			enterAction.actionPerformed(e)
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}