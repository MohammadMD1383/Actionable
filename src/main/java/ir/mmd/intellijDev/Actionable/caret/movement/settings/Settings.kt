package ir.mmd.intellijDev.Actionable.caret.movement.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException

import ir.mmd.intellijDev.Actionable.util.ext.isAllDistinct
import ir.mmd.intellijDev.Actionable.util.withService
import javax.swing.JComponent

/**
 * Settings [Configurable] UI for `Actionable > Caret > Movement`
 */
class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Movement"
	override fun getHelpTopic() = null
	override fun createComponent(): JComponent = ui?.component ?: UI().run { ui = this; component }
	
	override fun isModified() = withService<SettingsState, Boolean> {
		ui!!.wordSeparators != wordSeparators ||
			ui!!.wordSeparatorsBehaviour != wordSeparatorsBehaviour ||
			ui!!.hardStopCharacters != hardStopCharacters
	}
	
	override fun apply() {
		validateWordSeparators()
		validateHardStopCharacter()
		
		withService<SettingsState, Unit> {
			wordSeparators = ui!!.wordSeparators
			wordSeparatorsBehaviour = ui!!.wordSeparatorsBehaviour
			hardStopCharacters = ui!!.hardStopCharacters
		}
	}
	
	override fun reset() = withService<SettingsState, Unit> {
		ui!!.wordSeparators = wordSeparators
		ui!!.wordSeparatorsBehaviour = wordSeparatorsBehaviour
		ui!!.hardStopCharacters = hardStopCharacters
	}
	
	override fun disposeUIResources() {
		ui = null
	}
	
	/**
	 * validates the [SettingsState.wordSeparators] before saving it
	 *
	 * @throws ConfigurationException if required conditions for [SettingsState.wordSeparators] is not met
	 */
	private fun validateWordSeparators(): Unit = with(ui!!.wordSeparators) {
		when {
			isEmpty() -> throw ConfigurationException("Word Separators must not be Empty")
			isAllDistinct().not() -> throw ConfigurationException("Word Separators must contain distinct characters")
		}
	}
	
	/**
	 * validates the [SettingsState.hardStopCharacters] before saving it
	 *
	 * @throws ConfigurationException if required conditions for [SettingsState.hardStopCharacters] is not met
	 */
	private fun validateHardStopCharacter(): Unit = with(ui!!.hardStopCharacters) {
		if (!isAllDistinct()) throw ConfigurationException("Hard stop characters must contain distinct characters")
	}
}
