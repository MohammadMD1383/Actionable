package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.service

abstract class MoveCaretAction : AnAction() {
	fun moveCaretVirtually(
		caret: Caret,
		forward: Boolean,
		psiFile: PsiFile? = null
	): Int {
		if (psiFile == null) {
			val settings = service<SettingsState>()
			val cutil = caret.util
			
			cutil.moveCaret(
				settings.wordSeparators,
				settings.hardStopCharacters,
				settings.wordSeparatorsBehaviour,
				if (forward) FORWARD else BACKWARD
			)
			
			return cutil.offset
		} else {
			val offset = caret.offset
			val documentLength = caret.editor.document.textLength
			
			return if (offset == documentLength) {
				if (forward) {
					offset
				} else {
					psiFile.findElementAt(offset - 1)!!.textRange.startOffset
				}
			} else with(psiFile.findElementAt(offset)!!) {
				if (forward) {
					nextLeaf(true)?.textRange?.startOffset ?: textRange.endOffset
				} else {
					prevLeaf(true)?.textRange?.startOffset ?: 0
				}
			}
		}
	}
	
	fun moveCaretsWithSelection(
		carets: MutableList<Caret>,
		forward: Boolean,
		targetOffset: (Caret) -> Int
	) {
		val selectionsStart = carets.map { it.leadSelectionOffset } as MutableList
		
		if (!forward) {
			carets.reverse()
			selectionsStart.reverse()
		}
		
		carets.forEachIndexed { i, caret ->
			val offset = targetOffset(caret)
			
			carets.getOrNull(i + 1)?.let { nextCaret ->
				if (offset in nextCaret.selectionRangeCompat) return@forEachIndexed after {
					nextCaret.setSelection(selectionsStart[i], nextCaret.offset)
					selectionsStart[i + 1] = selectionsStart[i]
				}
			}
			
			caret.moveToOffset(offset)
			caret.setSelection(selectionsStart[i], caret.offset)
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
