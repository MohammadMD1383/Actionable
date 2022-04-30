package ir.mmd.intellijDev.Actionable.caret.editing.settings

import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.util.ext.runOnly
import javax.swing.JComponent

class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Editing"
	override fun createComponent(): JComponent? = UI().run { ui = this; component }
	
	override fun isModified() = SettingsState.getInstance().run {
		ui!!.isPasteActionHintsShown != showPasteActionHints
	}
	
	override fun apply() = SettingsState.getInstance().runOnly {
		showPasteActionHints = ui!!.isPasteActionHintsShown
	}
	
	override fun reset() = SettingsState.getInstance().runOnly {
		ui!!.isPasteActionHintsShown = showPasteActionHints
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
