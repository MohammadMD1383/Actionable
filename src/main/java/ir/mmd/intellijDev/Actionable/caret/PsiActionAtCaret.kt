package ir.mmd.intellijDev.Actionable.caret

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.elementAtOrBefore

abstract class PsiActionAtCaret(
	removeCarets: Boolean = true,
	inWriteAction: Boolean = false
) : ActionAtCaret<PsiActionAtCaret.Model, PsiElement>(removeCarets, inWriteAction) {
	class Model(
		caret: Caret,
		val psiElement: PsiElement
	) : ActionAtCaret.Model(caret)
	
	context(LazyEventContext)
	override fun createModel(caret: Caret): Model? {
		val psiElement = psiFile.elementAtOrBefore(caret, skipWhitespace = false) ?: return null
		return Model(caret, psiElement)
	}
	
	override fun distinctKey(model: Model) = model.psiElement
}
