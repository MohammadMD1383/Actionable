package ir.mmd.intellijDev.Actionable.text.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Settings State for `Actionable > Text`
 */
@State(
	name = "ir.mmd.intellijDev.Actionable.text.settings.SettingsState",
	storages = [Storage("Actionable.TextSettings.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {
	@Suppress("ConstPropertyName")
	object Defaults {
		const val preserveCase: Boolean = false
	}
	
	var preserveCase: Boolean = Defaults.preserveCase
	
	override fun getState() = this
	override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)
}
