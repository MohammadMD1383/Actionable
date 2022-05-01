package ir.mmd.intellijDev.Actionable.find.settings

import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.util.withFindSettings
import javax.swing.JComponent

class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Find"
	override fun createComponent(): JComponent? = UI().run { ui = this; component }
	
	override fun isModified(): Boolean = withFindSettings {
		isCaseSensitive != ui!!.isCaseSensitive
	}
	
	override fun apply() = withFindSettings {
		isCaseSensitive = ui!!.isCaseSensitive
	}
	
	override fun reset() = withFindSettings {
		ui!!.isCaseSensitive = isCaseSensitive
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
