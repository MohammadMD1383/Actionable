package ir.mmd.intellijDev.Actionable.text.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable

/**
 * Settings [Configurable] UI for `Actionable > Text`
 */
class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Text"
	override fun createComponent() = ui?.component ?: UI().run { ui = this; component }
	
	override fun isModified() = service<SettingsState>().run {
		ui!!.preserveCase != preserveCase
	}
	
	override fun apply() = service<SettingsState>().run {
		preserveCase = ui!!.preserveCase
	}
	
	override fun reset() = service<SettingsState>().run {
		ui!!.preserveCase = preserveCase
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
