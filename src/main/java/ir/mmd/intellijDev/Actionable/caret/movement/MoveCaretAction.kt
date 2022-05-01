package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.util.CaretUtil
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withMovementSettings

abstract class MoveCaretAction : AnAction() {
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
		
		if (dir == CaretUtil.BACKWARD) {
			carets.reverse()
			selectionStarts.reverse()
		}
		
		carets.forEachIndexed { i, caret ->
			val cutil = caret.util
			
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
