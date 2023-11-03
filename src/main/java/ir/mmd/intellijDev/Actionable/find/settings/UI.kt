package ir.mmd.intellijDev.Actionable.find.settings

import com.intellij.ui.dsl.builder.*
import ir.mmd.intellijDev.Actionable.ActionableBundle
import ir.mmd.intellijDev.Actionable.util.observableMutablePropertyOf

class UI {
	private val isCaseSensitiveProperty = observableMutablePropertyOf(SettingsState.Defaults.isCaseSensitive)
	var isCaseSensitive by isCaseSensitiveProperty
	
	val component = panel {
		row {
			checkBox(ActionableBundle.string("findPanel.caseSensitiveCheckBox.label"))
				.bindSelected(isCaseSensitiveProperty)
				.align(Align.FILL)
				.comment(ActionableBundle.string("findPanel.caseSensitiveCheckBox.comment"))
			
			button(ActionableBundle.string("global.defaultButton.label")) {
				isCaseSensitive = SettingsState.Defaults.isCaseSensitive
			}.align(AlignX.RIGHT + AlignY.CENTER)
		}
	}
}
