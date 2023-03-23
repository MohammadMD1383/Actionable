package ir.mmd.intellijDev.Actionable.caret.editing.settings

import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.util.service
import javax.swing.JComponent

/**
 * Settings [Configurable] UI for `Actionable > Caret > Editing`
 */
class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Editing"
	override fun createComponent(): JComponent = ui?.component ?: UI().run { ui = this; component }
	
	override fun isModified() = service<SettingsState>().run {
		ui!!.isPasteActionHintsShown != showPasteActionHints
	}
	
	override fun apply() = service<SettingsState>().run {
		showPasteActionHints = ui!!.isPasteActionHintsShown
	}
	
	override fun reset() = service<SettingsState>().run {
		ui!!.isPasteActionHintsShown = showPasteActionHints
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
