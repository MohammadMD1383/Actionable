package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.setActionAvailability
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.setPasteOffset

class SetWordCutPasteOffset : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;ct")
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class SetWordCopyPasteOffset : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;cp")
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class SetElementCutPasteOffset : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;ct")
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}

class SetElementCopyPasteOffset : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;cp")
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}
