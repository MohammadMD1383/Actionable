package ir.mmd.intellijDev.Actionable.lang.java.settings

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class AutoClassCaseState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java) {
	override fun SettingsState.get() = autoClassCaseEnabled
	override fun SettingsState.set(b: Boolean) {
		autoClassCaseEnabled = b
	}
}

class AutoInsertSemicolonState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java) {
	override fun SettingsState.get() = autoInsertSemicolonEnabled
	override fun SettingsState.set(b: Boolean) {
		autoInsertSemicolonEnabled = b
	}
}

class JITRefactoringEnabled : ProjectStateToggleAction<SettingsState>(SettingsState::class.java) {
	override fun SettingsState.get() = jitRefactoringEnabled
	override fun SettingsState.set(b: Boolean) {
		jitRefactoringEnabled = b
	}
}
