package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.returnBy
import ir.mmd.intellijDev.Actionable.util.withService

abstract class MoveCaretAction : AnAction() {
	fun moveCaretVirtually(
		caret: Caret,
		forward: Boolean,
		psiFile: PsiFile? = null
	): Int = if (psiFile == null) withService<SettingsState, Int> {
		val cutil = caret.util
		cutil.moveCaret(
			wordSeparators,
			hardStopCharacters,
			wordSeparatorsBehaviour,
			if (forward) FORWARD else BACKWARD
		)
		cutil.offset
	} else returnBy(caret.offset, caret.editor.document.textLength) { offset, length ->
		if (offset == length) {
			if (forward) offset
			else psiFile.findElementAt(offset - 1)!!.textRange.startOffset
		} else psiFile.findElementAt(offset)!!.run {
			if (forward) nextLeaf(true)?.textRange?.startOffset ?: textRange.endOffset
			else prevLeaf(true)?.textRange?.startOffset ?: 0
		}
	}
	
	fun moveCaretsWithSelection(
		carets: MutableList<Caret>,
		forward: Boolean,
		targetOffset: (Caret) -> Int
	) = returnBy(carets.map { it.leadSelectionOffset } as MutableList) { selectionStarts ->
		if (!forward) {
			carets.reverse()
			selectionStarts.reverse()
		}
		
		carets.forEachIndexed { i, caret ->
			val offset = targetOffset(caret)
			
			carets.getOrNull(i + 1)?.let { nextCaret ->
				if (offset in nextCaret.selectionRange) return@forEachIndexed after {
					nextCaret.setSelection(selectionStarts[i], nextCaret.offset)
					selectionStarts[i + 1] = selectionStarts[i]
				}
			}
			
			caret.moveToOffset(offset)
			caret.setSelection(selectionStarts[i], caret.offset)
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor }
}
