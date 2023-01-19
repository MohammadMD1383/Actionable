package ir.mmd.intellijDev.Actionable.selection.movement

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.ScrollType
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.service

// TODO: see move to next occurrence for more info on how the native action is implemented

abstract class MoveSelectionToOccurrence(
	private val isFirst: Boolean,
	private val isForward: Boolean
) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		val caret = editor.allCarets.run { if (isFirst) first() else last() }
		
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
			editor.scrollingModel.scrollTo(finalPosition, ScrollType.RELATIVE)
			if (caret.isValid) caret.setSelection(start, end)
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasEditorWith { allCaretsHasSelection } }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

@Keep
class MoveFirstSelectionToPreviousOccurrence : MoveSelectionToOccurrence(true, false)

@Keep
class MoveFirstSelectionToNextOccurrence : MoveSelectionToOccurrence(true, true)

@Keep
class MoveLastSelectionToPreviousOccurrence : MoveSelectionToOccurrence(false, false)

@Keep
class MoveLastSelectionToNextOccurrence : MoveSelectionToOccurrence(false, true)
