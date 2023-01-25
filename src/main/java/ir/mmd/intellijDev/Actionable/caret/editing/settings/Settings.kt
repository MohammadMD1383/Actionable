package ir.mmd.intellijDev.Actionable.caret.editing.settings

import com.intellij.openapi.options.Configurable

import ir.mmd.intellijDev.Actionable.util.withService
import javax.swing.JComponent


class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Editing"
	override fun getHelpTopic() = null
	override fun createComponent(): JComponent = ui?.component ?: UI().run { ui = this; component }
	
	override fun isModified() = withService<SettingsState, Boolean> {
		ui!!.isPasteActionHintsShown != showPasteActionHints
	}
	
	override fun apply() = withService<SettingsState, Unit> {
		showPasteActionHints = ui!!.isPasteActionHintsShown
	}
	
	override fun reset() = withService<SettingsState, Unit> {
		ui!!.isPasteActionHintsShown = showPasteActionHints
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
