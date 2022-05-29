package ir.mmd.intellijDev.Actionable.app

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.components.ApplicationComponent
import ir.mmd.intellijDev.Actionable.action.registerMacro
import ir.mmd.intellijDev.Actionable.text.macro.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.withService

class Startup : ApplicationComponent {
	@Deprecated("Deprecated in Java")
	override fun initComponent() = withService<SettingsState, Unit> {
		val actionManager = ActionManager.getInstance()
		macros.forEach { (name, macro) ->
			actionManager.registerMacro(name, macro)
		}
	}
}
