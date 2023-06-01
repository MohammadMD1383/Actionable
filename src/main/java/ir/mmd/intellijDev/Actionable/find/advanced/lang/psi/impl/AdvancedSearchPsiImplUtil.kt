package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.impl

import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiFactory
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiParameter

object AdvancedSearchPsiImplUtil {
	@JvmStatic
	fun isValidHost(element: AdvancedSearchPsiParameter) = true
	
	@JvmStatic
	fun updateText(element: AdvancedSearchPsiParameter, text: String): PsiLanguageInjectionHost {
		return AdvancedSearchPsiFactory.createParameterFromText(element.project, text).also {
			element.replace(AdvancedSearchPsiFactory.createParameterFromText(element.project, text))
		}
	}
	
	@JvmStatic
	fun createLiteralTextEscaper(element: AdvancedSearchPsiParameter): LiteralTextEscaper<out PsiLanguageInjectionHost> {
		return LiteralTextEscaper.createSimple(element)
	}
}
