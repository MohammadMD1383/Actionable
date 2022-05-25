package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.allCarets
import ir.mmd.intellijDev.Actionable.util.ext.psiFile
import ir.mmd.intellijDev.Actionable.util.ext.withEach

class MoveCaretToNextWord : MoveCaretAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, true))
	}
}

class MoveCaretToPreviousWord : MoveCaretAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, false))
	}
}

class MoveCaretToNextElement : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, true, e.psiFile))
	}
}

class MoveCaretToPreviousElement : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, false, e.psiFile))
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
