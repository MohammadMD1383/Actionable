package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext

abstract class OpenSettingsPageAction(private val clazz: Class<out Configurable>) : ActionBase(), DumbAware {
	context(LazyEventContext)
	override fun performAction() = ShowSettingsUtil.getInstance().showSettingsDialog(project, clazz)
}
