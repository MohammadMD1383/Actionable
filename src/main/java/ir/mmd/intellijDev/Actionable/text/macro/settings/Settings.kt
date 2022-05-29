package ir.mmd.intellijDev.Actionable.text.macro.settings

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.action.registerMacro
import ir.mmd.intellijDev.Actionable.action.unregisterMacro
import ir.mmd.intellijDev.Actionable.util.withService
import javax.swing.JComponent

class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Macro"
	override fun getHelpTopic() = null
	override fun createComponent(): JComponent = ui?.component ?: UI().run { ui = this; component }
	
	override fun isModified() = true
	
	override fun apply() = withService<SettingsState, Unit> {
		val actionManager = ActionManager.getInstance()
		
		macros.keys.forEach(actionManager::unregisterMacro)
		ui!!.macros.also { macros = it }.forEach { (name, macro) ->
			actionManager.registerMacro(name, macro)
		}
	}
	
	override fun reset() = withService<SettingsState, Unit> {
		ui!!.macros = macros
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}