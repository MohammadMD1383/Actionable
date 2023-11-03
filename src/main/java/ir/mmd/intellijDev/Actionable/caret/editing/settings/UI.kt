package ir.mmd.intellijDev.Actionable.caret.editing.settings

import com.intellij.ui.dsl.builder.*
import ir.mmd.intellijDev.Actionable.ActionableBundle
import ir.mmd.intellijDev.Actionable.util.observableMutablePropertyOf

class UI {
	private val isPasteActionHintsShownProperty = observableMutablePropertyOf(SettingsState.Defaults.showPasteActionHints)
	var isPasteActionHintsShown by isPasteActionHintsShownProperty
	
	val component = panel {
		row {
			checkBox(ActionableBundle.string("caretEditingPanel.showPasteActionHintsCheckBox.label"))
				.bindSelected(isPasteActionHintsShownProperty)
				.align(Align.FILL)
				.comment(ActionableBundle.string("caretEditingPanel.showPasteActionHintsCheckBox.comment"))
			
			button(ActionableBundle.string("global.defaultButton.label")) {
				isPasteActionHintsShown = SettingsState.Defaults.showPasteActionHints
			}.align(AlignX.RIGHT + AlignY.CENTER)
		}
	}
}
