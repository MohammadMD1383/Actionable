package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ShowSettingsUtil

abstract class OpenSettingsPageAction(private val clazz: Class<out Configurable>) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = ShowSettingsUtil.getInstance().showSettingsDialog(e.project, clazz)
	override fun isDumbAware() = true
}
