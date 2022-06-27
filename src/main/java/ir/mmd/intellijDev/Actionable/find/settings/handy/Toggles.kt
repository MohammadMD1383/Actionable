package ir.mmd.intellijDev.Actionable.find.settings.handy

import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.GlobalStateToggleAction

@Keep
class FindCaseSensitivityToggleAction : GlobalStateToggleAction<SettingsState>(SettingsState::class.java) {
	override fun SettingsState.get() = isCaseSensitive
	override fun SettingsState.set(b: Boolean) {
		isCaseSensitive = b
	}
}
