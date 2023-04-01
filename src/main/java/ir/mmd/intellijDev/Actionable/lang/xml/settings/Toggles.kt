package ir.mmd.intellijDev.Actionable.lang.xml.settings

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class CollapseEmptyTagOnBackspaceState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::collapseEmptyTagOnBackspaceEnabled)
class ExpandTagOnSmartEnterState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::expandTagOnSmartEnterEnabled)
