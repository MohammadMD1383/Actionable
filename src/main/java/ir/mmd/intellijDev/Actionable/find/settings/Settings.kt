package ir.mmd.intellijDev.Actionable.find.settings

import com.intellij.openapi.options.Configurable

import ir.mmd.intellijDev.Actionable.util.withService
import javax.swing.JComponent


class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Find"
	override fun getHelpTopic() = null
	override fun createComponent(): JComponent = ui?.component ?: UI().run { ui = this; component }
	
	override fun isModified(): Boolean = withService<SettingsState, Boolean> {
		isCaseSensitive != ui!!.isCaseSensitive
	}
	
	override fun apply() = withService<SettingsState, Unit> {
		isCaseSensitive = ui!!.isCaseSensitive
	}
	
	override fun reset() = withService<SettingsState, Unit> {
		ui!!.isCaseSensitive = isCaseSensitive
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
