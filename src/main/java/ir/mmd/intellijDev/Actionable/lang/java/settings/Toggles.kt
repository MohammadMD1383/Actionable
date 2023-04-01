package ir.mmd.intellijDev.Actionable.lang.java.settings

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class AutoClassCaseState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::autoClassCaseEnabled)
class AutoInsertSemicolonState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::autoInsertSemicolonEnabled)
class JITRefactoringEnabled : ProjectStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::jitRefactoringEnabled)
