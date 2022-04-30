package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.*
import ir.mmd.intellijDev.Actionable.util.ext.allCarets
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasEditor
import ir.mmd.intellijDev.Actionable.util.ext.withEach

abstract class CaretMoveAction : AnAction() {
	fun moveCarets(
		carets: List<Caret>,
		dir: Int
	) = withMovementSettings {
		carets.withEach {
			removeSelection()
			util.moveCaret(
				wordSeparators,
				hardStopCharacters,
				wordSeparatorsBehaviour,
				dir
			)
		}
	}
	
	fun moveCaretsWithSelection(
		carets: MutableList<Caret>,
		dir: Int
	) = withMovementSettings {
		val selectionStarts = carets.map { it.leadSelectionOffset } as MutableList
		
		if (dir == BACKWARD) {
			carets.reverse()
			selectionStarts.reverse()
		}
		
		carets.forEachIndexed { i, caret ->
			val cutil = CaretUtil(caret)
			
			cutil.moveCaret(
				wordSeparators,
				hardStopCharacters,
				wordSeparatorsBehaviour,
				dir,
				false
			)
			
			carets.getOrNull(i + 1)?.let { nextCaret ->
				if (cutil.offset in nextCaret.selectionRange) return@forEachIndexed after {
					nextCaret.setSelection(selectionStarts[i], nextCaret.offset)
					selectionStarts[i + 1] = selectionStarts[i]
				}
			}
			
			cutil.commit()
			caret.setSelection(selectionStarts[i], caret.offset)
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor }
}


class MoveCaretToNextWord : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCarets(e.allCarets, FORWARD)
}

class MoveCaretToPreviousWord : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCarets(e.allCarets, BACKWARD)
}

class MoveCaretToNextWordWithSelection : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, FORWARD)
}

class MoveCaretToPreviousWordWithSelection : CaretMoveAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, BACKWARD)
}
