package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import ir.mmd.intellijDev.Actionable.text.macro.settings.Settings

class OpenMacroSettingsPageAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		ShowSettingsUtil.getInstance().showSettingsDialog(e.project, Settings::class.java)
	}
	
	override fun isDumbAware() = true
}
