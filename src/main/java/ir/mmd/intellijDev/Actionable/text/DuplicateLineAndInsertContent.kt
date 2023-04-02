package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.TextRange
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.text.settings.SettingsState
import ir.mmd.intellijDev.Actionable.ui.showCustomDialog
import ir.mmd.intellijDev.Actionable.util.ext.addCaret
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.replaceRanges
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import javax.swing.JPanel

open class DuplicateLineAndInsertContent : ActionBase(), DumbAware {
	protected data class ReplacementModel(
		val extraAboveLines: Int,
		val extraBelowLines: Int,
		val isCasePreserving: Boolean,
		val replacements: MutableList<String>
	)
	
	context (LazyEventContext)
	protected open fun getReplacementModel(): ReplacementModel? {
		val textArea = JBTextArea(5, 200)
		val aboveCount = JBIntSpinner(0, 0, 1000)
		val belowCount = JBIntSpinner(0, 0, 1000)
		val casePreservingCheckbox = JBCheckBox("Case-Preserving", service<SettingsState>().preserveCase)
		val constraints = GridConstraints().apply {
			vSizePolicy = GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_CAN_SHRINK
			hSizePolicy = GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_CAN_SHRINK
			fill = GridConstraints.FILL_HORIZONTAL
			anchor = GridConstraints.ANCHOR_CENTER
		}
		
		val result = showCustomDialog(project, "Duplicate Line And Insert Content") {
			JPanel(GridLayoutManager(4, 2)).apply {
				add(textArea, constraints.apply { row = 0; column = 0; colSpan = 2 })
				add(casePreservingCheckbox, constraints.apply { row = 1; column = 0; colSpan = 2 })
				add(aboveCount, constraints.apply { row = 2; column = 0; colSpan = 1 })
				add(belowCount, constraints.apply { row = 2; column = 1; colSpan = 1 })
				add(JBLabel("Extra above lines"), constraints.apply { row = 3; column = 0; colSpan = 1 })
				add(JBLabel("Extra below lines"), constraints.apply { row = 3; column = 1; colSpan = 1 })
			}
		}
		
		return if (result == DialogWrapper.OK_EXIT_CODE && textArea.text.isNotBlank()) {
			ReplacementModel(
				aboveCount.number,
				belowCount.number,
				casePreservingCheckbox.isSelected,
				textArea.text.split('\n').toMutableList()
			)
		} else null
	}
	
	context (LazyEventContext)
	override fun performAction() {
		val replacementModel = getReplacementModel() ?: return
		val preserveCase = replacementModel.isCasePreserving
		val firstLineNumber = (document.getLineNumber(allCarets.first().selectionStart) - replacementModel.extraAboveLines).let { if (it < 0) 0 else it }
		val lastLineNumber = (document.getLineNumber(allCarets.last().selectionEnd) + replacementModel.extraBelowLines).let { if (it > document.lineCount) document.lineCount else it }
		var lineStartOffset = document.getLineStartOffset(firstLineNumber)
		val lineEndOffset = document.getLineEndOffset(lastLineNumber)
		val rawLine = document.getText(TextRange(lineStartOffset, lineEndOffset))
		val replacementRanges = allCarets.map {
			it.selectionStart - lineStartOffset..it.selectionEnd - lineStartOffset
		}
		
		runWriteCommandAction {
			replacementModel.replacements.removeFirst().let {
				val (newLine, ranges) = rawLine.replaceRanges(replacementRanges, it, preserveCase)
				
				document.replaceString(lineStartOffset, lineEndOffset, newLine)
				
				allCarets.forEachIndexed { i, caret ->
					val range = ranges[i]
					caret moveTo lineStartOffset + range.last
					caret.setSelection(lineStartOffset + range.first, lineStartOffset + range.last)
				}
				
				lineStartOffset += newLine.length
			}
			
			replacementModel.replacements.forEach {
				val (newLine, ranges) = rawLine.replaceRanges(replacementRanges, it, preserveCase)
				
				document.insertString(lineStartOffset++, "\n$newLine")
				
				ranges.forEach { range ->
					caretModel.addCaret(lineStartOffset + range.last)
						?.setSelection(lineStartOffset + range.first, lineStartOffset + range.last)
				}
				
				lineStartOffset += newLine.length
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class DuplicateLineAndPasteClipboardContentAction : DuplicateLineAndInsertContent() {
	context(LazyEventContext)
	override fun getReplacementModel(): ReplacementModel? {
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String)
			.split("\n").toMutableList()
		
		return if (contents.isNotEmpty()) ReplacementModel(0, 0, service<SettingsState>().preserveCase, contents) else null
	}
}
