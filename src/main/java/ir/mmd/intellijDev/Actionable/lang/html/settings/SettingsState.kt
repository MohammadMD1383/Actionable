package ir.mmd.intellijDev.Actionable.lang.html.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
	name = "ir.mmd.intellijDev.Actionable.lang.html.settings.SettingsState",
	storages = [Storage("Actionable.Lang.HTML.SettingsState.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {
	/**
	 * @see ir.mmd.intellijDev.Actionable.lang.html.type.ExpandTagOnType
	 */
	var expandTagOnTypeEnabled: Boolean = false
	
	override fun getState() = this
	override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)
}
