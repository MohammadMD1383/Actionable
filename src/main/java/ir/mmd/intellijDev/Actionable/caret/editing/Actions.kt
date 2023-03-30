package ir.mmd.intellijDev.Actionable.caret.editing

import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.caret.PsiActionAtCaret
import ir.mmd.intellijDev.Actionable.caret.WordActionAtCaret
import ir.mmd.intellijDev.Actionable.util.ext.copyToClipboard

class CopyWordAtCaret : WordActionAtCaret() {
	context(LazyEventContext)
	override fun doAction(model: Model) = model.word.copyToClipboard()
}

class CutWordAtCaret : WordActionAtCaret(true) {
	context(LazyEventContext)
	override fun doAction(model: Model) {
		model.word.copyToClipboard()
		document.deleteString(model.boundaries[0], model.boundaries[1])
	}
}

class DeleteWordAtCaretAction : WordActionAtCaret(true) {
	context(LazyEventContext)
	override fun doAction(model: Model) = document.deleteString(model.boundaries[0], model.boundaries[1])
}

class CopyElementAtCaret : PsiActionAtCaret() {
	context(LazyEventContext)
	override fun doAction(model: Model) = model.psiElement.text.copyToClipboard()
}

class CutElementAtCaret : PsiActionAtCaret(true) {
	context(LazyEventContext)
	override fun doAction(model: Model) = model.psiElement.run {
		text.copyToClipboard()
		delete()
	}
}

class DeleteElementAtCaretAction : PsiActionAtCaret(true) {
	context(LazyEventContext)
	override fun doAction(model: Model) = model.psiElement.delete()
}
