package ir.mmd.intellijDev.Actionable.text.settings

import com.intellij.ui.dsl.builder.*
import ir.mmd.intellijDev.Actionable.ActionableBundle
import ir.mmd.intellijDev.Actionable.util.observableMutablePropertyOf

class UI {
	private val preserveCaseProperty = observableMutablePropertyOf(SettingsState.Defaults.preserveCase)
	var preserveCase by preserveCaseProperty
	
	val component = panel {
		row {
			checkBox(ActionableBundle.string("textPanel.preserveCase.label"))
				.bindSelected(preserveCaseProperty)
				.align(Align.FILL)
			
			button(ActionableBundle.string("global.defaultButton.label")) {
				preserveCase = SettingsState.Defaults.preserveCase
			}.align(AlignX.RIGHT + AlignY.CENTER)
		}
	}
}
