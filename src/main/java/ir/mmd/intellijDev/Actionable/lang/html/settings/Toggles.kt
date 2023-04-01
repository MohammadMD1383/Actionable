package ir.mmd.intellijDev.Actionable.lang.html.settings

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class ExpandTagOnTypeState : ProjectStateToggleAction<SettingsState>(SettingsState::class.java, SettingsState::expandTagOnTypeEnabled)
