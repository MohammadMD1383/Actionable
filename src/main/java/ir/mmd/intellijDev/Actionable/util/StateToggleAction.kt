package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import ir.mmd.intellijDev.Actionable.util.ext.withService

sealed class StateToggleAction<S>(protected val clazz: Class<S>) : ToggleAction() {
	protected abstract fun S.get(): Boolean
	protected abstract fun S.set(b: Boolean)
	override fun isDumbAware() = true
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

abstract class ProjectStateToggleAction<S>(clazz: Class<S>) : StateToggleAction<S>(clazz) {
	override fun isSelected(e: AnActionEvent) = e.project!!.withService(clazz) { get() }
	override fun setSelected(e: AnActionEvent, state: Boolean) = e.project!!.withService(clazz) { set(state) }
}

abstract class GlobalStateToggleAction<S>(clazz: Class<S>) : StateToggleAction<S>(clazz) {
	override fun isSelected(e: AnActionEvent) = withService(clazz) { get() }
	override fun setSelected(e: AnActionEvent, state: Boolean) = withService(clazz) { set(state) }
}
