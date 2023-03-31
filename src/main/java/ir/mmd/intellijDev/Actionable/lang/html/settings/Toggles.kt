package ir.mmd.intellijDev.Actionable.lang.html.settings

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class ExpandTagOnTypeState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java) {
	override fun SettingsState.get() = expandTagOnTypeEnabled
	override fun SettingsState.set(b: Boolean) {
		expandTagOnTypeEnabled = b
	}
}
