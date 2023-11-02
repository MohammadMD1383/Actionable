package ir.mmd.intellijDev.Actionable.lang.java.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
	name = "ir.mmd.intellijDev.Actionable.lang.java.settings.SettingsState",
	storages = [Storage("Actionable.Lang.Java.SettingsState.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {
	/**
	 * @see ir.mmd.intellijDev.Actionable.lang.java.type.AutoClassCase
	 */
	var autoClassCaseEnabled: Boolean = true
	
	/**
	 * @see ir.mmd.intellijDev.Actionable.lang.java.type.AutoInsertSemicolon
	 */
	var autoInsertSemicolonEnabled: Boolean = true
	
	/**
	 * @see ir.mmd.intellijDev.Actionable.lang.java.type.JITRefactoringDelete
	 * @see ir.mmd.intellijDev.Actionable.lang.java.type.JITRefactoringInsert
	 */
	var jitRefactoringEnabled: Boolean = false
	
	override fun getState() = this
	override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)
}
