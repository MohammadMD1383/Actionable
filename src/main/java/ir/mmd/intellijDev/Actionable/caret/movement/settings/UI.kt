@file:Suppress("UnstableApiUsage", "PackageName")

package ir.mmd.intellijDev.Actionable.caret.movement.settings

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.buttonGroup
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import ir.mmd.intellijDev.Actionable.util.ext.addItem

class UI {
	private lateinit var wordSeparatorsField: JBTextField
	var wordSeparators: String
		get() = wordSeparatorsField.text
		set(value) {
			wordSeparatorsField.text = value
		}
	
	private lateinit var newLineCheckBox: JBCheckBox
	var newLineIncluded: Boolean
		get() = newLineCheckBox.isSelected
		set(value) {
			newLineCheckBox.isSelected = value
		}
	
	private var wordSeparatorsBehaviour = mutableListOf<JBRadioButton>()
	var selectedWordSeparatorsBehaviour: Int
		get() = wordSeparatorsBehaviour.indexOfFirst { it.isSelected }
		set(value) {
			wordSeparatorsBehaviour[value].isSelected = true
		}
	
	val ui: DialogPanel = panel {
		row("Word separators:") {
			wordSeparatorsField = textField()
				.resizableColumn()
				.horizontalAlign(HorizontalAlign.FILL)
				.component
			
			button("Default") {
				wordSeparators = SettingsState.Defaults.wordSeparators
			}
		}.layout(RowLayout.PARENT_GRID)
		
		row {
			cell()
			
			newLineCheckBox = checkBox("Include new line character (\\n)")
				.resizableColumn()
				.horizontalAlign(HorizontalAlign.FILL)
				.comment("Word separators will be used to determine where the caret should stop.")
				.component
			
			button("Default") {
				newLineIncluded = SettingsState.Defaults.newLineInclude
			}
		}.layout(RowLayout.PARENT_GRID)
		
		row(label = "Behaviour:") {
			panel {
				buttonGroup(this@UI::selectedWordSeparatorsBehaviour) {
					row {
						wordSeparatorsBehaviour addItem radioButton(
							"Stop at character type change",
							SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE
						).comment(
							"""
							If caret movement begins at a word character, it should go until next word separator character.<br>
							If caret movement begins at a word separator character, it should go until next word character.
						""".trimIndent()
						).component
					}
					row {
						wordSeparatorsBehaviour addItem radioButton(
							"Stop at next same character type",
							SettingsState.WSBehaviour.STOP_AT_NEXT_SAME_CHAR_TYPE
						).comment(
							"""
							If caret movement begins at a word character, it should go until next word character, after passing word separator characters.<br>
							If caret movement begins at a word separator character, it should go until next word separator character, after passing word characters.
						""".trimIndent()
						).component
					}
				}
			}.resizableColumn().horizontalAlign(HorizontalAlign.FILL)
			
			button("Default") {
				selectedWordSeparatorsBehaviour = SettingsState.Defaults.wordSeparatorsBehaviour
			}
			
			rowComment(
				"""
				<b>Glossary:</b><br>
				Word separator character: any character that you've defined in the <i>word separators</i> field.<br>
				Word character: all characters except those defined in the <i>word separators</i> field.
			""".trimIndent()
			)
		}.layout(RowLayout.PARENT_GRID)
	}
}
