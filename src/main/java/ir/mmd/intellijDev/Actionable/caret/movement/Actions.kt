package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.BACKWARD
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.FORWARD
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.moveCaret
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import java.util.stream.Collectors

/**
 * This class implements actions required by:<br></br>
 *
 *  * [MoveCaretToNextWord]
 *  * [MoveCaretToNextWordWithSelection]
 *  * [MoveCaretToPreviousWord]
 *  * [MoveCaretToPreviousWordWithSelection]
 *
 */
object Actions {
	/**
	 * common action availability criteria among these actions:
	 *
	 *  * [MoveCaretToNextWord]
	 *  * [MoveCaretToNextWordWithSelection]
	 *  * [MoveCaretToPreviousWord]
	 *  * [MoveCaretToPreviousWordWithSelection]
	 *
	 *
	 * @param e event of execution
	 */
	fun setActionAvailability(e: AnActionEvent) {
		val editor = e.getData(CommonDataKeys.EDITOR)
		e.presentation.isEnabled = editor != null
	}
	
	/**
	 * implementation for:
	 *
	 *  * [MoveCaretToNextWord]
	 *  * [MoveCaretToPreviousWord]
	 *
	 *
	 * @param e   event of mentioned actions
	 * @param dir see [CaretMovementHelper.moveCaret]
	 */
	fun moveCaret(
		e: AnActionEvent,
		dir: Int
	) {
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val settingsState = SettingsState.getInstance()
		val wordSeparators = settingsState.wordSeparators
		val hardStopCharacters = settingsState.hardStopCharacters
		for (caret in editor.caretModel.allCarets) {
			// if we omit this, when caret has selection, and we move it, the selection won't be cleared automatically.
			caret.removeSelection()
			val cutil = CaretMovementUtil(caret)
			moveCaret(
				cutil,
				wordSeparators,
				hardStopCharacters,
				settingsState.wordSeparatorsBehaviour,
				dir
			)
			
			/*
			 (in FORWARD movement direction)
			 because the caret stays at the beginning of the character that is associated to,
			 we move it one more character forward, to place it exactly after the character that we want.
			*/
			cutil.commit(if (dir == FORWARD) +1 else 0)
		}
	}
	
	/**
	 * implementation for:
	 *
	 *  * [MoveCaretToNextWordWithSelection]
	 *  * [MoveCaretToPreviousWordWithSelection]
	 *
	 *
	 * @param e   event of mentioned actions
	 * @param dir see [CaretMovementHelper.moveCaret]
	 */
	fun moveCaretWithSelection(
		e: AnActionEvent,
		dir: Int
	) {
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val settingsState = SettingsState.getInstance()
		val wordSeparators = settingsState.wordSeparators
		val hardStopCharacters = settingsState.hardStopCharacters
		val carets = editor.caretModel.allCarets
		val selectionStarts = carets.stream().map { obj: Caret? -> obj!!.leadSelectionOffset }.collect(Collectors.toList())
		
		if (dir == BACKWARD) {
			carets.reverse()
			selectionStarts.reverse()
		}
		
		for (i in carets.indices) {
			val caret = carets[i]
			val cutil = CaretMovementUtil(caret)
			
			moveCaret(
				cutil,
				wordSeparators,
				hardStopCharacters,
				settingsState.wordSeparatorsBehaviour,
				dir
			)
			
			if (i + 1 < carets.size) {
				val nextCaret = carets[i + 1]
				
				if (cutil.offset in nextCaret.selectionStart..nextCaret.selectionEnd) {
					nextCaret.setSelection(selectionStarts[i], nextCaret.offset)
					selectionStarts[i + 1] = selectionStarts[i]
					continue
				}
			}
			
			/* see the note of the same expression at `moveCarets` method */cutil.commit(if (dir == FORWARD) +1 else 0)
			caret.setSelection(selectionStarts[i]!!, caret.offset)
		}
	}
}
