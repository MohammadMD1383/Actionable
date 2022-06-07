package ir.mmd.intellijDev.Actionable.app

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.progress.ProgressIndicator
import ir.mmd.intellijDev.Actionable.action.registerMacro
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.text.macro.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.service

@Keep
class Startup : PreloadingActivity() {
	override fun preload(indicator: ProgressIndicator) {
		initMacros()
	}
	
	private fun initMacros() {
		val actionManager = ActionManager.getInstance()
		service<SettingsState>().macros.forEach { (name, macro) ->
			actionManager.registerMacro(name, macro)
		}
	}
}
