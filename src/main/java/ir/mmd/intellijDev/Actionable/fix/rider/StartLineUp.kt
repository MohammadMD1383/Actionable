package ir.mmd.intellijDev.Actionable.fix.rider

import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_ENTER
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.action
import ir.mmd.intellijDev.Actionable.util.ext.moveTo

class StartLineUp : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() {
		val enterAction = action(ACTION_EDITOR_ENTER)!!
		
		allCarets.asReversed().forEach { caret ->
			caret.removeSelection()
			val line = caret.logicalPosition.line
			
			if (line == 0) {
				runWriteCommandAction { document.insertString(0, "\n") }
				caret moveTo 0
			} else {
				caret moveTo document.getLineEndOffset(line - 1)
				enterAction.actionPerformed(event)
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}
