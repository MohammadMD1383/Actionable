package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.allCarets
import ir.mmd.intellijDev.Actionable.util.ext.psiFile
import ir.mmd.intellijDev.Actionable.util.ext.withEach

@Keep
class MoveCaretToNextWord : MoveCaretAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, true))
	}
}

@Keep
class MoveCaretToPreviousWord : MoveCaretAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, false))
	}
}

@Keep
class MoveCaretToNextElement : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, true, e.psiFile))
	}
}

@Keep
class MoveCaretToPreviousElement : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.withEach {
		removeSelection()
		moveToOffset(moveCaretVirtually(this, false, e.psiFile))
	}
}

@Keep
class MoveCaretToNextWordWithSelection : MoveCaretAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, true) {
		moveCaretVirtually(it, true)
	}
}

@Keep
class MoveCaretToPreviousWordWithSelection : MoveCaretAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, false) {
		moveCaretVirtually(it, false)
	}
}

@Keep
class MoveCaretToNextElementWithSelection : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, true) {
		moveCaretVirtually(it, true, e.psiFile)
	}
}

@Keep
class MoveCaretToPreviousElementWithSelection : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, false) {
		moveCaretVirtually(it, false, e.psiFile)
	}
}
