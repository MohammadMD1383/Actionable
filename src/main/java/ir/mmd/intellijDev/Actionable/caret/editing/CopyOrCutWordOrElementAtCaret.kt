package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.copyElementAtCaret
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.copyWordAtCaret
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.setActionAvailability

class CutWordAtCaret : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, true)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class CutElementAtCaret : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, true)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class CopyWordAtCaret : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, false)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class CopyElementAtCaret : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, false)
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}
