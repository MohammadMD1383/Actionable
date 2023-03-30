package ir.mmd.intellijDev.Actionable.toggle

import ir.mmd.intellijDev.Actionable.find.settings.SettingsState

import ir.mmd.intellijDev.Actionable.util.GlobalStateToggleAction

class FindCaseSensitivityToggleAction : GlobalStateToggleAction<SettingsState>(SettingsState::class.java) {
	override fun SettingsState.get() = isCaseSensitive
	override fun SettingsState.set(b: Boolean) {
		isCaseSensitive = b
	}
}
