package ir.mmd.intellijDev.Actionable.selection.movement

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class MoveSelectionToOccurrence(
	private val isFirst: Boolean,
	private val isForward: Boolean
) : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() {
		val caret = allCarets.run { if (isFirst) first() else last() }
		
		val (found, start, end) = FindManager.getInstance(project).findString(
			document.immutableCharSequence,
			caret.run { if (isForward) selectionEnd else selectionStart },
			FindModel().apply {
				isCaseSensitive = service<SettingsState>().isCaseSensitive
				isForward = this@MoveSelectionToOccurrence.isForward
				stringToFind = caret.selectedText!!
			}
		)
		
		if (found) {
			val finalPosition = editor.offsetToLogicalPosition(start + (caret.offset - caret.selectionStart))
			caret.removeSelection()
			caret moveTo finalPosition
			scrollingModel.scrollTo(finalPosition, ScrollType.RELATIVE)
			if (caret.isValid) caret.setSelection(start, end)
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && allCarets.haveSelection
}

class MoveFirstSelectionToPreviousOccurrence : MoveSelectionToOccurrence(true, false)
class MoveFirstSelectionToNextOccurrence : MoveSelectionToOccurrence(true, true)
class MoveLastSelectionToPreviousOccurrence : MoveSelectionToOccurrence(false, false)
class MoveLastSelectionToNextOccurrence : MoveSelectionToOccurrence(false, true)
