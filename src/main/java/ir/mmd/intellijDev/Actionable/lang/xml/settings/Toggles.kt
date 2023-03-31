package ir.mmd.intellijDev.Actionable.lang.xml.settings

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class CollapseEmptyTagOnBackspaceState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java) {
	override fun SettingsState.get() = collapseEmptyTagOnBackspaceEnabled
	override fun SettingsState.set(b: Boolean) {
		collapseEmptyTagOnBackspaceEnabled = b
	}
}
