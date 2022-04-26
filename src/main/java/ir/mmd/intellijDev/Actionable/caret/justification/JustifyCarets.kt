package ir.mmd.intellijDev.Actionable.caret.justification

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.caret.justification.Actions.justifyCarets
import ir.mmd.intellijDev.Actionable.caret.justification.Actions.setActionAvailability

class JustifyCaretsEndAndShift : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = justifyCarets(e, JustifyCaretUtil::justifyCaretsEndWithShifting)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class JustifyCaretsStart : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = justifyCarets(e, JustifyCaretUtil::justifyCaretsStart)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class JustifyCaretsEnd : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = justifyCarets(e, JustifyCaretUtil::justifyCaretsEnd)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}
