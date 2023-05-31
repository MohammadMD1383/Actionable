package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.patterns.InitialPatternCondition
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiParameter
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement

open class AdvancedSearchElementPattern<T : PsiElement, Self : AdvancedSearchElementPattern<T, Self>> : PsiElementPattern<T, Self> {
	constructor(aClass: Class<T>) : super(aClass)
	constructor(condition: InitialPatternCondition<T>) : super(condition)
	
	companion object {
		@JvmStatic
		fun psiElement() = Capture(PsiElement::class.java)
		
		@JvmStatic
		fun <T : PsiElement> psiElement(aClass: Class<T>) = Capture(aClass)
		
		@JvmStatic
		fun psiElement(type: IElementType) = psiElement().withElementType(type)
		
		@JvmStatic
		fun psiParameter() = AdvancedSearchParameterPattern()
	}
	
	class Capture<T : PsiElement> : AdvancedSearchElementPattern<T, Capture<T>> {
		constructor(aClass: Class<T>) : super(aClass)
		constructor(condition: InitialPatternCondition<T>) : super(condition)
	}
}

class AdvancedSearchParameterPattern : AdvancedSearchElementPattern<AdvancedSearchPsiParameter, AdvancedSearchParameterPattern>(AdvancedSearchPsiParameter::class.java) {
	/**
	 * specify [text] with '$'
	 */
	fun withVariableText(text: String): AdvancedSearchParameterPattern {
		return with(object : PatternCondition<AdvancedSearchPsiParameter>("AdvancedSearchParameterPattern.withVariableText") {
			override fun accepts(t: AdvancedSearchPsiParameter, context: ProcessingContext): Boolean {
				val statement = t.parentOfType<AdvancedSearchPsiStatement>() ?: return false
				return statement.variable.text == text
			}
		})
	}
	
	fun withIdentifierText(text: String): AdvancedSearchParameterPattern {
		return with(object : PatternCondition<AdvancedSearchPsiParameter?>("AdvancedSearchParameterPattern.withIdentifierText") {
			override fun accepts(t: AdvancedSearchPsiParameter, context: ProcessingContext?): Boolean {
				val statement = t.parentOfType<AdvancedSearchPsiStatement>() ?: return false
				return statement.identifier?.text == text
			}
		})
	}
}
