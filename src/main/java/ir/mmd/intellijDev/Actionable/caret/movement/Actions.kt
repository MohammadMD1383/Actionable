package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.*


class MoveCaretToNextWord : MoveCaretAction() {
	override fun isDumbAware() = true
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		caret.removeSelection()
		caret moveTo moveCaretVirtually(caret, true)
	}
}


class MoveCaretToPreviousWord : MoveCaretAction() {
	override fun isDumbAware() = true
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


class MoveCaretToNextWordWithSelection : MoveCaretAction() {
	override fun isDumbAware() = true
	
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, true) {
		moveCaretVirtually(it, true)
	}
}


class MoveCaretToPreviousWordWithSelection : MoveCaretAction() {
	override fun isDumbAware() = true
	
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, false) {
		moveCaretVirtually(it, false)
	}
}


class MoveCaretToNextElementWithSelection : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, true) {
		moveCaretVirtually(it, true, e.psiFile)
	}
}


class MoveCaretToPreviousElementWithSelection : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, false) {
		moveCaretVirtually(it, false, e.psiFile)
	}
}

class MoveCaretToNextSameElement : MoveCaretToSameElement() {
	override fun PsiElement.getNextLeafElement() = nextLeafNoWhitespace()
}

class MoveCaretToPreviousSameElement : MoveCaretToSameElement() {
	override fun PsiElement.getNextLeafElement() = prevLeafNoWhitespace()
}
