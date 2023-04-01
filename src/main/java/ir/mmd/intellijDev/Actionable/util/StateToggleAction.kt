package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import ir.mmd.intellijDev.Actionable.util.ext.service
import kotlin.reflect.KMutableProperty1

/**
 * Base class of [ProjectStateToggleAction] and [GlobalStateToggleAction]
 *
 * @param clazz usually a [com.intellij.openapi.components.PersistentStateComponent]
 */
sealed class StateToggleAction<T>(
	protected val clazz: Class<T>,
	private val property: KMutableProperty1<T, Boolean>
) : ToggleAction() {
	abstract fun getService(e: AnActionEvent): T
	
	override fun isSelected(e: AnActionEvent) = property.get(getService(e))
	override fun setSelected(e: AnActionEvent, state: Boolean) = property.set(getService(e), state)
	
	override fun isDumbAware() = true
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

/**
 * This class should be inherited for toggle actions that are project-wide
 *
 * @param clazz usually a [com.intellij.openapi.components.PersistentStateComponent]
 */
abstract class ProjectStateToggleAction<T>(
	clazz: Class<T>,
	property: KMutableProperty1<T, Boolean>
) : StateToggleAction<T>(clazz, property) {
	override fun getService(e: AnActionEvent) = e.project!!.service(clazz)
}

/**
 * This class should be inherited for toggle actions that are application-wide.
 *
 * @param clazz usually a [com.intellij.openapi.components.PersistentStateComponent]
 */
abstract class GlobalStateToggleAction<T>(
	clazz: Class<T>,
	property: KMutableProperty1<T, Boolean>
) : StateToggleAction<T>(clazz, property) {
	override fun getService(e: AnActionEvent) = service(clazz)
}
