package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent

class CutWordAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, true)
}

class CutElementAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, true)
}

class CopyWordAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, false)
}

class CopyElementAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, false)
}

class SetWordCutPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;ct")
}

class SetWordCopyPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;cp")
}

class SetElementCutPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;ct")
}

class SetElementCopyPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;cp")
}
