package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.parameter
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.stringLiteral
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStringLiteral

class AdvancedSearchMultiHostInjector : MultiHostInjector {
	override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
		javaInterfaceForImplements(context, registrar)
	}
	
	private fun javaInterfaceForImplements(context: PsiElement, registrar: MultiHostRegistrar) :Boolean {
		val condition = stringLiteral()
			.inside(parameter()
				.withIdentifierText("implements")
				.withTopLevelProperty("language", "java"))
		
		if (condition.accepts(context)) {
			registrar.startInjecting(JavaLanguage.INSTANCE)
				.addPlace(
					"class A implements ", " { }",
					context as PsiLanguageInjectionHost,
					TextRange.from(1, context.text.lastIndex - 1)
				)
				.doneInjecting()
			
			return true
		}
		
		return false
	}
	
	override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
		return mutableListOf(AdvancedSearchPsiStringLiteral::class.java)
	}
}
