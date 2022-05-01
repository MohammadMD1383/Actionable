package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.ext.allCarets

class MoveCaretToNextWord : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCarets(e.allCarets, FORWARD)
}

class MoveCaretToPreviousWord : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCarets(e.allCarets, BACKWARD)
}

class MoveCaretToNextWordWithSelection : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, FORWARD)
}

class MoveCaretToPreviousWordWithSelection : MoveCaretAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretsWithSelection(e.allCarets, BACKWARD)
}
