package ir.mmd.intellijDev.Actionable.lang.xml.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
	name = "ir.mmd.intellijDev.Actionable.lang.xml.settings.SettingsState",
	storages = [Storage("Actionable.Lang.XML.SettingsState.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {
	/**
	 * @see ir.mmd.intellijDev.Actionable.lang.xml.type.CollapseEmptyTagOnBackspace
	 */
	var collapseEmptyTagOnBackspaceEnabled: Boolean = true
	
	/**
	 * @see ir.mmd.intellijDev.Actionable.lang.xml.type.ExpandTagSmartEnterProcessor
	 */
	var expandTagOnSmartEnterEnabled: Boolean = true
	
	override fun getState() = this
	override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)
}
