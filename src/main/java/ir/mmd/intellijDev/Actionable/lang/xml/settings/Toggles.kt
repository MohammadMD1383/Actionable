package ir.mmd.intellijDev.Actionable.lang.xml.settings

import ir.mmd.intellijDev.Actionable.util.GlobalStateToggleAction

class CollapseEmptyTagOnBackspaceState : GlobalStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::collapseEmptyTagOnBackspaceEnabled)
class ExpandTagOnSmartEnterState : GlobalStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::expandTagOnSmartEnterEnabled)
