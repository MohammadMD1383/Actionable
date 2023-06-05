package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStringLiteral
import ir.mmd.intellijDev.Actionable.util.then
import org.intellij.lang.regexp.RegExpLanguage

private fun innerStringLiteralRangeOf(element: PsiElement): TextRange {
	return TextRange.from(1, element.text.lastIndex - 1)
}

class AdvancedSearchMultiHostInjector : MultiHostInjector {
	override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
		javaInterfaceForImplementsAndExtends(context, registrar) or
			javaClassForExtends(context, registrar) or
			javaMethodParam(context, registrar) or
			regexpForMatches(context, registrar)
		// todo: optimize patterns
	}
	
	private fun regexpForMatches(context: PsiElement, registrar: MultiHostRegistrar): Boolean {
		val condition = stringLiteral()
			.inside(parameter()
				.withIdentifier(".+-matches".toRegex()))
		
		return condition.accepts(context) then {
			registrar.startInjecting(RegExpLanguage.INSTANCE)
				.addPlace(
					null, null,
					context as PsiLanguageInjectionHost,
					innerStringLiteralRangeOf(context)
				).doneInjecting()
		}
	}
	
	private fun javaMethodParam(context: PsiElement, registrar: MultiHostRegistrar): Boolean {
		val condition = stringLiteral()
			.withSuperParent(3, statement()
				.withVariable("\$method", checkParent = true)
				.withIdentifier("has-param")
				.withTopLevelProperty("language", "java"))
		
		return condition.accepts(context) then {
			registrar.startInjecting(JavaLanguage.INSTANCE)
				.addPlace(
					"class A { void m(", ") { } }",
					context as PsiLanguageInjectionHost,
					innerStringLiteralRangeOf(context)
				).doneInjecting()
		}
	}
	
	private fun javaClassForExtends(context: PsiElement, registrar: MultiHostRegistrar): Boolean {
		val condition = stringLiteral()
			.withSuperParent(3, statement()
				.withVariable("\$class", checkParent = true)
				.withIdentifier("extends", "extends-directly")
				.withTopLevelProperty("language", "java"))
		
		return condition.accepts(context) then {
			registrar.startInjecting(JavaLanguage.INSTANCE)
				.addPlace(
					"class A extends ", " { }",
					context as PsiLanguageInjectionHost,
					innerStringLiteralRangeOf(context)
				).doneInjecting()
		}
	}
	
	private fun javaInterfaceForImplementsAndExtends(context: PsiElement, registrar: MultiHostRegistrar): Boolean {
		val condition = stringLiteral()
			.withSuperParent(3,
				(statement().withVariable("\$class", checkParent = true).withIdentifier("implements", "implements-directly") or
					statement().withVariable("\$interface", checkParent = true).withIdentifier("extends", "extends-directly"))
					.withTopLevelProperty("language", "java")
			)
		
		return condition.accepts(context) then {
			registrar.startInjecting(JavaLanguage.INSTANCE)
				.addPlace(
					"class A implements ", " { }",
					context as PsiLanguageInjectionHost,
					innerStringLiteralRangeOf(context)
				).doneInjecting()
		}
	}
	
	override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
		return mutableListOf(AdvancedSearchPsiStringLiteral::class.java)
	}
}
