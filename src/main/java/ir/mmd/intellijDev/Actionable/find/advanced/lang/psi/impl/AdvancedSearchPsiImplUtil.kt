package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.impl

import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.parentOfType
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

fun getKey(element: AdvancedSearchPsiTopLevelProperty): String {
	return element.identifier.text
}

fun getValue(element: AdvancedSearchPsiTopLevelProperty): String? {
	return element.stringLiteral?.content
}

fun getContent(element: AdvancedSearchPsiStringLiteral): String {
	return element.text.innerSubString(1, 1)
}

fun isRaw(element: AdvancedSearchPsiStringLiteral): Boolean {
	return element is AdvancedSearchPsiRawString
}

fun getLanguagePsiProperty(element: AdvancedSearchPsiTopLevelProperties): AdvancedSearchPsiTopLevelProperty? {
	return element.topLevelPropertyList.find { it.key == "language" }
}

fun findPsiPropertyByKey(element: AdvancedSearchPsiTopLevelProperties, key: String, ignoreCase:Boolean): AdvancedSearchPsiTopLevelProperty? {
	return element.topLevelPropertyList.find { it.key.equals(key, ignoreCase) }
}

fun findPsiPropertyByKey(element: AdvancedSearchPsiTopLevelProperties, key: String): AdvancedSearchPsiTopLevelProperty? {
	return findPsiPropertyByKey(element, key, true)
}

fun getPsiVariable(element: AdvancedSearchPsiStatement): PsiElement? {
	return element.node.findChildByType(AdvancedSearchTypes.VARIABLE)?.psi
}

fun getPsiIdentifier(element: AdvancedSearchPsiStatement): PsiElement? {
	return element.node.findChildByType(AdvancedSearchTypes.IDENTIFIER)?.psi
}

fun getPsiParameters(element: AdvancedSearchPsiStatement): AdvancedSearchPsiParameters? {
	return element.node.findChildByType(AdvancedSearchTypes.PARAMETERS)?.psi as AdvancedSearchPsiParameters?
}

fun getVariable(element: AdvancedSearchPsiStatement): String? {
	return element.psiVariable?.text
}

fun getIdentifier(element: AdvancedSearchPsiStatement): String? {
	return element.psiIdentifier?.text
}

fun getParameters(element: AdvancedSearchPsiStatement): List<AdvancedSearchPsiParameter> {
	return element.psiParameters?.parameterList ?: emptyList()
}

fun getParentStatement(element: AdvancedSearchPsiStatement): AdvancedSearchPsiStatement? {
	return element.parentOfType<AdvancedSearchPsiStatement>()
}
