package ir.mmd.intellijDev.Actionable.app

import com.intellij.diagnostic.LoadingState
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.components.ApplicationComponent
import ir.mmd.intellijDev.Actionable.action.registerMacro
import ir.mmd.intellijDev.Actionable.app.Compatibility.Version
import ir.mmd.intellijDev.Actionable.text.macro.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.service

@Suppress("DEPRECATION")
class Startup : ApplicationComponent {
	@Suppress("OVERRIDE_DEPRECATION")
	override fun initComponent() {
		initMacros()
	}
	
	private fun initMacros() {
		if (Compatibility.isCompatibleWith(Version._193_5233_102_))
			if (!LoadingState.COMPONENTS_LOADED.isOccurred) return
		
		val actionManager = ActionManager.getInstance()
		service<SettingsState>().macros.forEach { (name, macro) ->
			actionManager.registerMacro(name, macro)
		}
	}
}
