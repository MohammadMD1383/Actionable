package ir.mmd.intellijDev.Actionable.caret.editing

import com.goide.psi.GoParameterDeclaration
import com.intellij.lang.javascript.psi.JSParameter
import com.intellij.lang.javascript.psi.ecma6.TypeScriptParameter
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.psi.PsiActionAtCaret
import ir.mmd.intellijDev.Actionable.util.ext.copyToClipboard
import org.jetbrains.kotlin.psi.KtParameter

abstract class ParamAtCaretAction(inWriteAction: Boolean = false) : PsiActionAtCaret(inWriteAction) {
	context (LazyEventContext)
	abstract fun execute(param: PsiElement)
	
	context(LazyEventContext)
	override fun doAction(model: Model) {
		when (psiFile.fileType.name.lowercase()) {
			"java" -> model.psiElement.parentOfType<PsiParameter>()
			"kotlin" -> model.psiElement.parentOfType<KtParameter>()
			"javascript" -> model.psiElement.parentOfType<JSParameter>()
			"typescript" -> model.psiElement.parentOfType<TypeScriptParameter>()
			"go" -> model.psiElement.parentOfType<GoParameterDeclaration>()
			else -> null
		}?.let {
			execute(it)
		}
	}
}

class CopyParamAtCaretAction : ParamAtCaretAction() {
	context(LazyEventContext)
	override fun execute(param: PsiElement) = param.text.copyToClipboard()
}

class CutParamAtCaretAction : ParamAtCaretAction(true) {
	context(LazyEventContext)
	override fun execute(param: PsiElement) {
		param.text.copyToClipboard()
		param.delete()
	}
}

class DeleteParamAtCaretAction : ParamAtCaretAction(true) {
	context(LazyEventContext)
	override fun execute(param: PsiElement) = param.delete()
}
