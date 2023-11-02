package ir.mmd.intellijDev.Actionable.caret.movement.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Settings State for `Actionable > Caret > Editing`
 */
@State(
	name = "ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState",
	storages = [Storage("Actionable.CaretMovementSettingsState.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState?> {
	/**
	 * **enum**
	 *
	 * for behaviour of `move caret ...` actions
	 *
	 * see [UI] for what these constants mean
	 */
	enum class WSBehaviour {
		/**
		 * Example of moving caret with this mode
		 * ```
		 * hello| world from java
		 * hello |world from java
		 * hello world| from java
		 * hello world |from java
		 * ...
		 * ```
		 */
		StopAtCharTypeChange,
		
		/**
		 * Example of moving caret with this mode
		 * ```
		 * hello| world from java
		 * hello world| from java
		 * hello world from| java
		 * hello world from java|
		 * ...
		 * ```
		 */
		StopAtNextSameCharType
	}
	
	enum class SEMBehaviour {
		Start, Offset, End
	}
	
	/**
	 * this class holds default values for settings
	 */
	object Defaults {
		const val wordSeparators: String = "`~!@#$%^&*()-=+[{]}\\|;:'\",.<>/?\n\t"
		
		@JvmField
		val wordSeparatorsBehaviour: WSBehaviour = WSBehaviour.StopAtCharTypeChange
		const val hardStopSeparators: String = " "
		
		@JvmField
		val sameElementMovementBehaviour: SEMBehaviour = SEMBehaviour.Start
	}
	
	var wordSeparators: String = Defaults.wordSeparators
	var wordSeparatorsBehaviour: WSBehaviour = Defaults.wordSeparatorsBehaviour
	var hardStopCharacters: String = Defaults.hardStopSeparators
	var sameElementMovementBehaviour: SEMBehaviour = Defaults.sameElementMovementBehaviour
	
	override fun getState() = this
	override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)
}
