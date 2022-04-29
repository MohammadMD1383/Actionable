package ir.mmd.intellijDev.Actionable.find.settings

import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.util.runOnly
import javax.swing.JComponent

class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Find"
	override fun createComponent(): JComponent? = UI().run { ui = this; component }
	
	override fun isModified(): Boolean = SettingsState.getInstance().run {
		isCaseSensitive != ui!!.isCaseSensitive
	}
	
	override fun apply() = SettingsState.getInstance().runOnly {
		isCaseSensitive = ui!!.isCaseSensitive
	}
	
	override fun reset() = SettingsState.getInstance().runOnly {
		ui!!.isCaseSensitive = isCaseSensitive
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
