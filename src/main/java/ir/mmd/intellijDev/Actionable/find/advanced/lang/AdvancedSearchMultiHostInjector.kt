package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.psiParameter
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiParameter

class AdvancedSearchMultiHostInjector : MultiHostInjector {
	override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
		if (psiParameter().withIdentifierText("implements").accepts(context)) {
			registrar.startInjecting(JavaLanguage.INSTANCE)
				.addPlace("class A implements ", " { }", context as PsiLanguageInjectionHost, context.textRange)
				.doneInjecting()
		}
	}
	
	override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
		return mutableListOf(AdvancedSearchPsiParameter::class.java)
	}
}
