package ir.mmd.intellijDev.Actionable.text.macro.lang.psi

import com.intellij.patterns.InitialPatternCondition
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

open class MacroTemplateElementPattern<T : PsiElement, Self : MacroTemplateElementPattern<T, Self>> : PsiElementPattern<T, Self> {
	constructor(aClass: Class<T>) : super(aClass)
	constructor(condition: InitialPatternCondition<T>) : super(condition)
	
	companion object {
		@JvmStatic
		fun psiElement() = Capture(PsiElement::class.java)
		
		@JvmStatic
		fun <T : PsiElement> psiElement(aClass: Class<T>) = Capture(aClass)
		
		@JvmStatic
		fun psiElement(type: IElementType) = psiElement().withElementType(type)
	}
	
	class Capture<T : PsiElement> : MacroTemplateElementPattern<T, Capture<T>> {
		constructor(aClass: Class<T>) : super(aClass)
		constructor(condition: InitialPatternCondition<T>) : super(condition)
	}
}
