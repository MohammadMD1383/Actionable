package ir.mmd.intellijDev.Actionable.lang.xml.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.ui.FeatureDescriptor
import ir.mmd.intellijDev.Actionable.ui.FeatureToggleSettingsPage

class Settings : Configurable {
	private var ui: FeatureToggleSettingsPage<SettingsState>? = null
	
	override fun getDisplayName() = "XML"
	
	override fun createComponent() = ui ?: FeatureToggleSettingsPage(
		service<SettingsState>(),
		listOf(
			FeatureDescriptor("Collapse Empty Tag On Backspace", SettingsState::collapseEmptyTagOnBackspaceEnabled),
			FeatureDescriptor("Expand Tag On Smart Enter", SettingsState::expandTagOnSmartEnterEnabled),
		)
	).also { ui = it }
	
	override fun isModified() = ui!!.isModified()
	override fun apply() = ui!!.save()
	override fun reset() = ui!!.reset()
	
	override fun disposeUIResources() {
		ui = null
	}
}
