package ir.mmd.intellijDev.Actionable.typing.html.state

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import ir.mmd.intellijDev.Actionable.util.ext.withService

class ExpandTagOnTypeState : ToggleAction() {
	override fun isSelected(e: AnActionEvent) = e.project!!.withService<State, Boolean> { expandTagOnTypeEnabled }
	override fun setSelected(e: AnActionEvent, state: Boolean) = e.project!!.withService<State, Unit> { expandTagOnTypeEnabled = state }
}
