package ir.mmd.intellijDev.Actionable.caret.editing.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Settings State for `Actionable > Caret > Editing`
 */
@State(
	name = "ir.mmd.intellijDev.Actionable.caret.editing.settings",
	storages = [Storage("Actionable.CaretEditingSettingsState.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {
	/**
	 * This class holds the default values for the settings
	 */
	@Suppress("ConstPropertyName")
	object Defaults {
		const val showPasteActionHints: Boolean = true
	}
	
	var showPasteActionHints: Boolean = Defaults.showPasteActionHints
	
	override fun getState() = this
	override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)
}
