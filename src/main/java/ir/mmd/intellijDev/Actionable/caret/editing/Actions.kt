package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.psi.PsiActionAtCaret
import ir.mmd.intellijDev.Actionable.text.WordActionAtCaret
import ir.mmd.intellijDev.Actionable.util.ext.enableIf

class CutWordAtCaret : CaretEditingAction() {
	override fun isDumbAware() = true
	context (LazyEventContext)
	override fun perform(caret: Caret) = copyWordAtCaret(true)
}

class CutElementAtCaret : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = copyElementAtCaret(true)
}

class CopyWordAtCaret : CaretEditingAction() {
	override fun isDumbAware() = true
	context(LazyEventContext)
	override fun perform(caret: Caret) = copyWordAtCaret(false)
}

class CopyElementAtCaret : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = copyElementAtCaret(false)
}

class DeleteElementAtCaretAction : PsiActionAtCaret(true) {
	context(LazyEventContext)
	override fun doAction(model: Model) = model.psiElement.delete()
}

class DeleteWordAtCaretAction : WordActionAtCaret(true) {
	context(LazyEventContext)
	override fun doAction(model: Model) = document.deleteString(model.boundaries[0], model.boundaries[1])
}

class SetWordCutPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("wd;ct")
}

class SetWordCopyPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("wd;cp")
}

class SetElementCutPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("el;ct")
}

class SetElementCopyPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("el;cp")
}

class CancelPasteAction : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = editor.removeScheduledPasteAction()
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor && editor.getUserData(scheduledPasteActionKind) != null }
}
