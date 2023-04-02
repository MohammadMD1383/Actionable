package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiFile
import com.intellij.psi.util.nextLeaf
import com.intellij.psi.util.prevLeaf
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.afterDoing
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.util

abstract class MoveCaretAction : MultiCaretAction() {
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
				if (offset in nextCaret.selectionRange) return@forEachIndexed afterDoing {
					nextCaret.setSelection(selectionsStart[i], nextCaret.offset)
					selectionsStart[i + 1] = selectionsStart[i]
				}
			}
			
			caret.moveToOffset(offset)
			caret.setSelection(selectionsStart[i], caret.offset)
		}
	}
	
	context(LazyEventContext)
	override fun perform(caret: Caret) = Unit
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class MoveCaretToNextWord : MoveCaretAction(), DumbAware {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		caret.removeSelection()
		caret moveTo moveCaretVirtually(caret, true)
	}
}

class MoveCaretToPreviousWord : MoveCaretAction(), DumbAware {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		caret.removeSelection()
		caret moveTo moveCaretVirtually(caret, false)
	}
}

class MoveCaretToNextElement : MoveCaretAction() {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		caret.removeSelection()
		caret moveTo moveCaretVirtually(caret, true, psiFile)
	}
}

class MoveCaretToPreviousElement : MoveCaretAction() {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		caret.removeSelection()
		caret moveTo moveCaretVirtually(caret, false, psiFile)
	}
}

class MoveCaretToNextWordWithSelection : MoveCaretAction(), DumbAware {
	context (LazyEventContext)
	override fun performAction() = moveCaretsWithSelection(allCarets.toMutableList(), true) {
		moveCaretVirtually(it, true)
	}
}

class MoveCaretToPreviousWordWithSelection : MoveCaretAction(), DumbAware {
	
	context (LazyEventContext)
	override fun performAction() = moveCaretsWithSelection(allCarets.toMutableList(), false) {
		moveCaretVirtually(it, false)
	}
}

class MoveCaretToNextElementWithSelection : MoveCaretAction() {
	context (LazyEventContext)
	override fun performAction() = moveCaretsWithSelection(allCarets.toMutableList(), true) {
		moveCaretVirtually(it, true, psiFile)
	}
}

class MoveCaretToPreviousElementWithSelection : MoveCaretAction() {
	context (LazyEventContext)
	override fun performAction() = moveCaretsWithSelection(allCarets.toMutableList(), false) {
		moveCaretVirtually(it, false, psiFile)
	}
}
