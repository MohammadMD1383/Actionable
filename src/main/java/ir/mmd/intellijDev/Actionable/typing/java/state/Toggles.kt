package ir.mmd.intellijDev.Actionable.typing.java.state

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import ir.mmd.intellijDev.Actionable.util.ext.withService

class AutoClassCaseState : ToggleAction() {
	override fun isDumbAware() = true
	override fun isSelected(e: AnActionEvent) = e.project!!.withService<State, Boolean> { autoClassCaseEnabled }
	override fun setSelected(e: AnActionEvent, state: Boolean) = e.project!!.withService<State, Unit> { autoClassCaseEnabled = state }
}

class AutoInsertSemicolonState : ToggleAction() {
	override fun isDumbAware() = true
	override fun isSelected(e: AnActionEvent) = e.project!!.withService<State, Boolean> { autoInsertSemicolonEnabled }
	override fun setSelected(e: AnActionEvent, state: Boolean) = e.project!!.withService<State, Unit> { autoInsertSemicolonEnabled = state }
}
