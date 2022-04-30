package ir.mmd.intellijDev.Actionable.caret.movement.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import ir.mmd.intellijDev.Actionable.util.ext.runOnly
import ir.mmd.intellijDev.Actionable.util.ext.isAllDistinct
import javax.swing.JComponent

/**
 * This class is the connector between [UI] and [SettingsState]
 */
class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Movement"
	override fun createComponent(): JComponent? = UI().run { ui = this; component }
	
	override fun isModified() = SettingsState.getInstance().run {
		(ui!!.wordSeparators != wordSeparators) or
			(ui!!.wordSeparatorsBehaviour != wordSeparatorsBehaviour) or
			(ui!!.hardStopCharacters != hardStopCharacters)
	}
	
	override fun apply() {
		validateWordSeparators()
		validateHardStopCharacter()
		
		SettingsState.getInstance().runOnly {
			wordSeparators = ui!!.wordSeparators
			wordSeparatorsBehaviour = ui!!.wordSeparatorsBehaviour
			hardStopCharacters = ui!!.hardStopCharacters
		}
	}
	
	override fun reset() {
		SettingsState.getInstance().runOnly {
			ui!!.wordSeparators = wordSeparators
			ui!!.wordSeparatorsBehaviour = wordSeparatorsBehaviour
			ui!!.hardStopCharacters = hardStopCharacters
		}
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
		when {
			isAllDistinct().not() -> throw ConfigurationException("Hard stop characters must contain distinct characters")
		}
	}
}
