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

private fun commonCriteriaForIdentifiers(variable: Array<String>, identifier: Array<String>, language: String, checkParent: Boolean): AdvancedSearchStringLiteralPattern.Capture<AdvancedSearchPsiStringLiteral> {
	return stringLiteral().withSuperParent(3, statement()
		.withVariable(*variable, checkParent = checkParent)
		.withIdentifier(*identifier)
		.withTopLevelProperty("language", language))
}

class AdvancedSearchMultiHostInjector : MultiHostInjector {
	override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
		javaInterfaceForImplementsAndExtends(context, registrar) or
			javaClassForExtends(context, registrar) or
			javaTypeForSuper(context, registrar) or
			javaMethodParam(context, registrar) or
			regexpForMatches(context, registrar)
		// todo: optimize patterns
	}
	
	private fun javaTypeForSuper(context: PsiElement, registrar: MultiHostRegistrar): Boolean {
		val condition = commonCriteriaForIdentifiers(
			variable = arrayOf("\$class", "\$interface", "\$type"),
			identifier = arrayOf("super-of", "direct-super-of"),
			language = "java",
			checkParent = true
		)
		
		return condition.accepts(context) then {
			registrar.startInjecting(JavaLanguage.INSTANCE)
				.addPlace(
					"class A<T extends ", "> { }",
					context as PsiLanguageInjectionHost,
					innerStringLiteralRangeOf(context)
				).doneInjecting()
		}
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
		val condition = commonCriteriaForIdentifiers(
			variable = arrayOf("\$method"),
			identifier = arrayOf("has-param", "with-param"),
			language = "java",
			checkParent = true
		)
		
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
		val condition = commonCriteriaForIdentifiers(
			variable = arrayOf("\$class"),
			identifier = arrayOf("extends", "extends-directly"),
			language = "java",
			checkParent = true
		)
		
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

// private fun MultiHostRegistrar.getInjectedFile(): PsiFile? {
// 	return try {
// 		val result = javaClass.getDeclaredMethod("getInjectedResult").run {
// 			isAccessible = true
// 			invoke(this@getInjectedFile)
// 		}
// 		val files = result.javaClass.getDeclaredField("files").get(result) as List<*>
// 		files[0] as PsiFile
// 	} catch (ignored: Exception) {
// 		null
// 	}
// }
