package ir.mmd.intellijDev.Actionable.caret.editing

import com.goide.psi.GoParameterDeclaration
import com.intellij.lang.javascript.psi.JSParameter
import com.intellij.lang.javascript.psi.ecma6.TypeScriptParameter
import com.intellij.psi.PsiParameter
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.psi.PsiActionAtCaret
import ir.mmd.intellijDev.Actionable.util.ext.parentOfType
import org.jetbrains.kotlin.psi.KtParameter

class DeleteParamAtCaretAction : PsiActionAtCaret(true) {
	context(LazyEventContext)
	override fun doAction(model: Model) {
		when (psiFile.fileType.name.lowercase()) {
			"java" -> model.psiElement.parentOfType<PsiParameter>()?.delete()
			"kotlin" -> model.psiElement.parentOfType<KtParameter>()?.delete()
			"javascript" -> model.psiElement.parentOfType<JSParameter>()?.delete()
			"typescript" -> model.psiElement.parentOfType<TypeScriptParameter>()?.delete()
			"go" -> model.psiElement.parentOfType<GoParameterDeclaration>()?.delete()
		}
	}
}
