package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import ir.mmd.intellijDev.Actionable.util.ext.withService

/**
 * Base class of [ProjectStateToggleAction] and [GlobalStateToggleAction]
 *
 * @param clazz usually a [com.intellij.openapi.components.PersistentStateComponent]
 */
sealed class StateToggleAction<S>(protected val clazz: Class<S>) : ToggleAction() {
	/**
	 * Get toggle's current state
	 */
	protected abstract fun S.get(): Boolean
	
	/**
	 * Set toggle's state
	 */
	protected abstract fun S.set(b: Boolean)
	
	override fun isDumbAware() = true
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

/**
 * This class should be inherited for toggle actions that are project-wide
 *
 * @param clazz usually a [com.intellij.openapi.components.PersistentStateComponent]
 */
abstract class ProjectStateToggleAction<S>(clazz: Class<S>) : StateToggleAction<S>(clazz) {
	override fun isSelected(e: AnActionEvent) = e.project!!.withService(clazz) { get() }
	override fun setSelected(e: AnActionEvent, state: Boolean) = e.project!!.withService(clazz) { set(state) }
}

/**
 * This class should be inherited for toggle actions that are application-wide
 *
 * @param clazz usually a [com.intellij.openapi.components.PersistentStateComponent]
 */
abstract class GlobalStateToggleAction<S>(clazz: Class<S>) : StateToggleAction<S>(clazz) {
	override fun isSelected(e: AnActionEvent) = withService(clazz) { get() }
	override fun setSelected(e: AnActionEvent, state: Boolean) = withService(clazz) { set(state) }
}
