package ir.mmd.intellijDev.Actionable.caret.editing.settings

import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.util.withEditingSettings
import javax.swing.JComponent

class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Editing"
	override fun createComponent(): JComponent? = UI().run { ui = this; component }
	
	override fun isModified() = withEditingSettings {
		ui!!.isPasteActionHintsShown != showPasteActionHints
	}
	
	override fun apply() = withEditingSettings {
		showPasteActionHints = ui!!.isPasteActionHintsShown
	}
	
	override fun reset() = withEditingSettings {
		ui!!.isPasteActionHintsShown = showPasteActionHints
	}
	
	override fun disposeUIResources() {
		ui = null
	}
}
