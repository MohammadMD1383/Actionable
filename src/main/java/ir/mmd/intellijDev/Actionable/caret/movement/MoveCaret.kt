package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.caret.movement.Actions.*
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.*


class MoveCaretToPreviousWordWithSelection : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretWithSelection(e, BACKWARD)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class MoveCaretToPreviousWord : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaret(e, BACKWARD)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class MoveCaretToNextWordWithSelection : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaretWithSelection(e, FORWARD)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class MoveCaretToNextWord : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = moveCaret(e, FORWARD)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}
