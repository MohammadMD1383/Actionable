package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.*

class CutWordAtCaret : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, true)
}

class CutElementAtCaret : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, true)
}

class CopyWordAtCaret : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, false)
}

class CopyElementAtCaret : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, false)
}

class SetWordCutPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;ct")
}

class SetWordCopyPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;cp")
}

class SetElementCutPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;ct")
}

class SetElementCopyPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;cp")
}

class CancelPasteAction : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.removeScheduledPasteAction()
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { getUserData(scheduledPasteActionKind).isNotNull } }
}
