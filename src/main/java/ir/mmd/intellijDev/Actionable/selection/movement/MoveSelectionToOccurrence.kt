package ir.mmd.intellijDev.Actionable.selection.movement

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.ScrollType
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState

import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.service

abstract class MoveSelectionToOccurrence(
	private val isFirst: Boolean,
	private val isForward: Boolean
) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
		val caret = allCarets.run { if (isFirst) first() else last() }
		
		val (found, start, end) = FindManager.getInstance(e.project).findString(
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
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor && allCarets.all { it.hasSelection() } }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}


class MoveFirstSelectionToPreviousOccurrence : MoveSelectionToOccurrence(true, false)


class MoveFirstSelectionToNextOccurrence : MoveSelectionToOccurrence(true, true)


class MoveLastSelectionToPreviousOccurrence : MoveSelectionToOccurrence(false, false)


class MoveLastSelectionToNextOccurrence : MoveSelectionToOccurrence(false, true)
