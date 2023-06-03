package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.impl

import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.*
import ir.mmd.intellijDev.Actionable.util.ext.innerSubString

fun isValidHost(element: AdvancedSearchPsiStringLiteral) = true

fun updateText(element: AdvancedSearchPsiStringLiteral, text: String): PsiLanguageInjectionHost {
	return AdvancedSearchPsiFactory.createRawStringFromText(element.project, text).also {
		element.replace(AdvancedSearchPsiFactory.createParameterFromText(element.project, text))
	}
}

fun createLiteralTextEscaper(element: AdvancedSearchPsiStringLiteral): LiteralTextEscaper<out PsiLanguageInjectionHost> {
	return LiteralTextEscaper.createSimple(element)
}

fun getPropertyKey(element: AdvancedSearchPsiTopLevelProperty): String {
	return element.identifier.text
}

fun getPropertyValue(element: AdvancedSearchPsiTopLevelProperty): String? {
	return element.stringLiteral?.stringText
}

fun getStringText(element: AdvancedSearchPsiStringLiteral): String {
	return element.text.innerSubString(1, 1)
}

fun isRawString(element: AdvancedSearchPsiStringLiteral): Boolean {
	return element is AdvancedSearchPsiRawString
}

fun getLanguageProperty(element: AdvancedSearchPsiTopLevelProperties): AdvancedSearchPsiTopLevelProperty? {
	return element.topLevelPropertyList.find { it.propertyKey == "language" }
}
