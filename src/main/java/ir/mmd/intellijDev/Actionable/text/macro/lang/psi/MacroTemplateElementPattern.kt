package ir.mmd.intellijDev.Actionable.text.macro.lang.psi

import com.intellij.patterns.InitialPatternCondition
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext

open class MacroTemplateElementPattern<T : PsiElement, Self : MacroTemplateElementPattern<T, Self>> : PsiElementPattern<T, Self> {
	constructor(aClass: Class<T>) : super(aClass)
	constructor(condition: InitialPatternCondition<T>) : super(condition)
	
	companion object {
		@JvmStatic
		fun psiElement() = Capture(PsiElement::class.java)
	}
	
	fun betweenDollars(): Self {
		return with(object : PatternCondition<PsiElement>("betweenDollars") {
			override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
				return t.elementType == MacroTemplateTypes.DOLLAR && t.prevSibling.elementType == MacroTemplateTypes.DOLLAR
			}
		})
	}
	
	class Capture<T : PsiElement> : MacroTemplateElementPattern<T, Capture<T>> {
		constructor(aClass: Class<T>) : super(aClass)
		constructor(condition: InitialPatternCondition<T>) : super(condition)
	}
}
