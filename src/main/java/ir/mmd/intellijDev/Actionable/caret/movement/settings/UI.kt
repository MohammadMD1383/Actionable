package ir.mmd.intellijDev.Actionable.caret.movement.settings

import com.intellij.openapi.observable.properties.ObservableMutableProperty
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import ir.mmd.intellijDev.Actionable.ActionableBundle
import ir.mmd.intellijDev.Actionable.util.observableMutablePropertyOf

class UI {
	private val newLineIncludedProperty = observableMutablePropertyOf('\n' in SettingsState.Defaults.wordSeparators)
	private var newLineIncluded by newLineIncludedProperty
	
	private val tabIncludedProperty = observableMutablePropertyOf('\t' in SettingsState.Defaults.wordSeparators)
	private var tabIncluded by tabIncludedProperty
	
	private val wordSeparatorsProperty = observableMutablePropertyOf(SettingsState.Defaults.wordSeparators.replace("[\n\t]".toRegex(), ""))
	var wordSeparators: String
		get() {
			return StringBuilder(wordSeparatorsProperty.get()).apply {
				if (newLineIncluded) {
					append("\n")
				}
				
				if (tabIncluded) {
					append("\t")
				}
			}.toString()
		}
		set(value) {
			newLineIncluded = value.indexOf('\n') != -1
			tabIncluded = value.indexOf('\t') != -1
			wordSeparatorsProperty.set(value.replace("[\n\t]".toRegex(), ""))
		}
	
	private val wordSeparatorsBehaviourProperty = observableMutablePropertyOf(SettingsState.Defaults.wordSeparatorsBehaviour)
	var wordSeparatorsBehaviour by wordSeparatorsBehaviourProperty
	
	private val hardStopNewLineIncludedProperty = observableMutablePropertyOf('\n' in SettingsState.Defaults.hardStopSeparators)
	private var hardStopNewLineIncluded by newLineIncludedProperty
	
	private val hardStopTabIncludedProperty = observableMutablePropertyOf('\t' in SettingsState.Defaults.hardStopSeparators)
	private var hardStopTabIncluded by tabIncludedProperty
	
	private val hardStopCharactersProperty = observableMutablePropertyOf(SettingsState.Defaults.hardStopSeparators.replace("[\n\t]".toRegex(), ""))
	var hardStopCharacters: String
		get() {
			return StringBuilder(hardStopCharactersProperty.get()).apply {
				if (hardStopNewLineIncluded) {
					append("\n")
				}
				
				if (hardStopTabIncluded) {
					append("\t")
				}
			}.toString()
		}
		set(value) {
			hardStopNewLineIncluded = value.indexOf('\n') != -1
			hardStopTabIncluded = value.indexOf('\t') != -1
			hardStopCharactersProperty.set(value.replace("[\n\t]".toRegex(), ""))
		}
	
	private val sameElementMovementBehaviourProperty = observableMutablePropertyOf(SettingsState.Defaults.sameElementMovementBehaviour)
	var sameElementMovementBehaviour by sameElementMovementBehaviourProperty
	
	val component = panel {// todo add default button
		group(ActionableBundle.string("caretMovementPanel.wordSeparators.label")) {
			row {
				textField()
					.bindText(wordSeparatorsProperty as ObservableMutableProperty<String>)
					.align(Align.FILL)
			}
			
			row {
				checkBox(ActionableBundle.string("caretMovementPanel.wordSeparators.newLineIncluded.label"))
					.bindSelected(newLineIncludedProperty as ObservableMutableProperty<Boolean>)
					.align(Align.FILL)
			}
			
			row {
				checkBox(ActionableBundle.string("caretMovementPanel.wordSeparators.tabIncluded.label"))
					.bindSelected(tabIncludedProperty as ObservableMutableProperty<Boolean>)
					.align(Align.FILL)
			}
			
			row {
				comment(ActionableBundle.string("caretMovementPanel.wordSeparators.comment"))
			}
			
			row {
				comment(ActionableBundle.string("caretMovementPanel.wordSeparatorsBehaviour.comment"))
			}
		}
		
		group(ActionableBundle.string("caretMovementPanel.wordSeparatorsBehaviour.label")) {
			buttonsGroup {
				row {
					radioButton(ActionableBundle.string("caretMovementPanel.wordSeparatorsBehaviour.option1.label"), SettingsState.WSBehaviour.StopAtCharTypeChange)
						.comment(ActionableBundle.string("caretMovementPanel.wordSeparatorsBehaviour.option1.comment"))
				}
				
				row {
					radioButton(ActionableBundle.string("caretMovementPanel.wordSeparatorsBehaviour.option2.label"), SettingsState.WSBehaviour.StopAtNextSameCharType)
						.comment(ActionableBundle.string("caretMovementPanel.wordSeparatorsBehaviour.option2.comment"))
				}
			}.bind(wordSeparatorsBehaviourProperty, SettingsState.WSBehaviour::class.java)
		}
		
		group(ActionableBundle.string("caretMovementPanel.hardStopSeparators.label")) {
			row {
				textField()
					.bindText(hardStopCharactersProperty as ObservableMutableProperty<String>)
					.align(Align.FILL)
			}
			
			row {
				checkBox(ActionableBundle.string("caretMovementPanel.wordSeparators.newLineIncluded.label"))
					.bindSelected(hardStopNewLineIncludedProperty as ObservableMutableProperty<Boolean>)
					.align(Align.FILL)
			}
			
			row {
				checkBox(ActionableBundle.string("caretMovementPanel.wordSeparators.tabIncluded.label"))
					.bindSelected(hardStopTabIncludedProperty as ObservableMutableProperty<Boolean>)
					.align(Align.FILL)
			}
			
			row {
				comment(ActionableBundle.string("caretMovementPanel.hardStopSeparators.comment"))
			}
		}
		
		group(ActionableBundle.string("caretMovementPanel.sameElementMovementBehaviour.label")) {
			buttonsGroup {
				row {
					radioButton(ActionableBundle.string("caretMovementPanel.sameElementMovementBehaviour.option1.label"), SettingsState.SEMBehaviour.Start)
						.comment(ActionableBundle.string("caretMovementPanel.sameElementMovementBehaviour.option1.comment"))
				}
				
				row {
					radioButton(ActionableBundle.string("caretMovementPanel.sameElementMovementBehaviour.option2.label"), SettingsState.SEMBehaviour.Offset)
						.comment(ActionableBundle.string("caretMovementPanel.sameElementMovementBehaviour.option2.comment"))
				}
				
				row {
					radioButton(ActionableBundle.string("caretMovementPanel.sameElementMovementBehaviour.option3.label"), SettingsState.SEMBehaviour.End)
						.comment(ActionableBundle.string("caretMovementPanel.sameElementMovementBehaviour.option3.comment"))
				}
			}.bind(sameElementMovementBehaviourProperty, SettingsState.SEMBehaviour::class.java)
		}
	}
}
