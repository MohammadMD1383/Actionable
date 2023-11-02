package ir.mmd.intellijDev.Actionable.find.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Settings State for `Actionable > Find`
 */
@State(
	name = "ir.mmd.intellijDev.Actionable.find.settings.SettingsState",
	storages = [Storage("Actionable.FindSettingsState.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {
	/**
	 * This class contains the default values for the settings
	 */
	@Suppress("ConstPropertyName")
	object Defaults {
		const val isCaseSensitive: Boolean = true
	}
	
	var isCaseSensitive: Boolean = Defaults.isCaseSensitive
	
	override fun getState() = this
	override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)
}
