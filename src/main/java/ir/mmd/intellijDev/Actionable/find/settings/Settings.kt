package ir.mmd.intellijDev.Actionable.find.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable

import javax.swing.JComponent

/**
 * Settings [Configurable] UI for `Actionable > Find`
 */
class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Find"
	override fun createComponent(): JComponent = ui?.component ?: UI().run { ui = this; component }
	
	override fun isModified(): Boolean = service<SettingsState>().run {
		isCaseSensitive != ui!!.isCaseSensitive
	}
	
	override fun apply() = service<SettingsState>().run {
		isCaseSensitive = ui!!.isCaseSensitive
	}
	
	override fun reset() = service<SettingsState>().run {
		ui!!.isCaseSensitive = isCaseSensitive
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
