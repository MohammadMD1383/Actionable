package ir.mmd.intellijDev.Actionable.app

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.actionSystem.ActionManager
import ir.mmd.intellijDev.Actionable.action.registerMacro
import ir.mmd.intellijDev.Actionable.text.macro.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.withService

class Startup : AppLifecycleListener {
	override fun appFrameCreated(commandLineArgs: MutableList<String>) = withService<SettingsState, Unit> {
		val actionManager = ActionManager.getInstance()
		macros.forEach { (name, macro) ->
			actionManager.registerMacro(name, macro)
		}
	}
}