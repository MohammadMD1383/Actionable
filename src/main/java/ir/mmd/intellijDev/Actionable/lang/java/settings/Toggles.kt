package ir.mmd.intellijDev.Actionable.lang.java.settings

import ir.mmd.intellijDev.Actionable.util.GlobalStateToggleAction

class AutoClassCaseState : GlobalStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::autoClassCaseEnabled)
class AutoInsertSemicolonState : GlobalStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::autoInsertSemicolonEnabled)
class JITRefactoringEnabled : GlobalStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::jitRefactoringEnabled)
