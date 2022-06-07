package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*

@Keep
class CutWordAtCaret : CaretEditingAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, true)
}

@Keep
class CutElementAtCaret : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, true)
}

@Keep
class CopyWordAtCaret : CaretEditingAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, false)
}

@Keep
class CopyElementAtCaret : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, false)
}

@Keep
class SetWordCutPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;ct")
}

@Keep
class SetWordCopyPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;cp")
}

@Keep
class SetElementCutPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;ct")
}

@Keep
class SetElementCopyPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;cp")
}

@Keep
class CancelPasteAction : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.removeScheduledPasteAction()
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { getUserData(scheduledPasteActionKind).isNotNull } }
}
