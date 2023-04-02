package ir.mmd.intellijDev.Actionable.lang.html.settings

import ir.mmd.intellijDev.Actionable.util.GlobalStateToggleAction

class ExpandTagOnTypeState : GlobalStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::expandTagOnTypeEnabled)
