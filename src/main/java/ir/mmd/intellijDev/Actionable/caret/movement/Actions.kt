package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.moveCaret
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.editor
import ir.mmd.intellijDev.Actionable.util.selectionRange

abstract class CaretMoveAction : AnAction() {
	fun moveCaret(
		e: AnActionEvent,
		dir: Int
	) {
		val settingsState = SettingsState.getInstance()
		e.editor!!.caretModel.allCarets.forEach {
			it.removeSelection()
			val cutil = CaretUtil(it)
			moveCaret(
				cutil,
				settingsState.wordSeparators,
				settingsState.hardStopCharacters,
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
	
	fun moveCaretWithSelection(
		e: AnActionEvent,
		dir: Int
	) {
		val settingsState = SettingsState.getInstance()
		val carets = e.editor!!.caretModel.allCarets
		val selectionStarts = carets.map { it.leadSelectionOffset } as MutableList<Int>
		
		if (dir == BACKWARD) {
			carets.reverse()
			selectionStarts.reverse()
		}
		
		carets.forEachIndexed { i, caret ->
			val cutil = CaretUtil(caret)
			
			moveCaret(
				cutil,
				settingsState.wordSeparators,
				settingsState.hardStopCharacters,
				settingsState.wordSeparatorsBehaviour,
				dir
			)
			
			carets.getOrNull(i + 1)?.let { nextCaret ->
				if (cutil.offset in nextCaret.selectionRange) {
					nextCaret.setSelection(selectionStarts[i], nextCaret.offset)
					selectionStarts[i + 1] = selectionStarts[i]
					return@forEachIndexed
				}
			}
			
			/* see the note of the same expression at `moveCarets` method */
			cutil.commit(if (dir == FORWARD) +1 else 0)
			caret.setSelection(selectionStarts[i], caret.offset)
		}
	}
	
	override fun update(e: AnActionEvent) {
		e.presentation.isEnabled = e.editor != null
	}
}


class MoveCaretToPreviousWordWithSelection : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretWithSelection(e, BACKWARD)
}

class MoveCaretToPreviousWord : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaret(e, BACKWARD)
}

class MoveCaretToNextWordWithSelection : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretWithSelection(e, FORWARD)
}

class MoveCaretToNextWord : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaret(e, FORWARD)
}
