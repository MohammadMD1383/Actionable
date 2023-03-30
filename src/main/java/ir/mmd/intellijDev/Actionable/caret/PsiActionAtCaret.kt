package ir.mmd.intellijDev.Actionable.caret

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.elementAtOrBefore

abstract class PsiActionAtCaret(inWriteAction: Boolean = false) : ActionAtCaret<PsiActionAtCaret.Model, PsiElement>(inWriteAction) {
	class Model(
		override val caret: Caret,
		val psiElement: PsiElement
	) : IModel
	
	context(LazyEventContext)
	override fun mapCaret(caret: Caret): Model? {
		val psiElement = psiFile.elementAtOrBefore(caret) ?: return null
		return Model(caret, psiElement)
	}
	
	override fun distinctKey(model: Model) = model.psiElement
}
