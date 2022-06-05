package ir.mmd.intellijDev.Actionable.typing

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import ir.mmd.intellijDev.Actionable.util.ext.withService

abstract class StateToggleAction<S>(private val clazz: Class<S>) : ToggleAction() {
	abstract fun S.get(): Boolean
	abstract fun S.set(b: Boolean)
	
	override fun isDumbAware() = true
	override fun isSelected(e: AnActionEvent) = e.project!!.withService(clazz) { get() }
	override fun setSelected(e: AnActionEvent, state: Boolean) = e.project!!.withService(clazz) { set(state) }
}
