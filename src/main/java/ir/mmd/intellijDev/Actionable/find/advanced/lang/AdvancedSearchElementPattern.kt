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
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStringLiteral
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes
import org.jetbrains.kotlin.psi.psiUtil.children

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
		fun variable() = psiElement(AdvancedSearchTypes.VARIABLE)
		
		@JvmStatic
		fun identifier() = psiElement(AdvancedSearchTypes.IDENTIFIER)
		
		@JvmStatic
		fun stringLiteral() = AdvancedSearchStringLiteralPattern.Capture(AdvancedSearchPsiStringLiteral::class.java)
		
		@JvmStatic
		fun stringSequence() = psiElement(AdvancedSearchTypes.STRING_SEQ)
		
		@JvmStatic
		fun parameter() = AdvancedSearchParameterPattern()
	}
	
	inline fun <reified T : PsiElement> inside(): Self {
		return inside(T::class.java)
	}
	
	inline fun with(debugName: String?, crossinline accepts: (t: T, context: ProcessingContext) -> Boolean): Self {
		return with(object : PatternCondition<T>(debugName) {
			override fun accepts(t: T, context: ProcessingContext) = accepts(t, context)
		})
	}
	
	infix fun or(pattern: AdvancedSearchElementPattern<T, Self>): Capture<PsiElement> {
		return psiElement().with("(${this@AdvancedSearchElementPattern}) or ($pattern)") { t, context ->
			this.accepts(t, context) || pattern.accepts(t, context)
		}
	}
	
	/**
	 * specify [value] without quotes
	 *
	 * set [value] to null to just match against property
	 */
	fun withTopLevelProperty(property: String, value: String? = null): Self {
		return with("AdvancedSearchElementPattern.withTopLevelProperty") { t, _ ->
			val file = t.containingFile
			if (file !is AdvancedSearchFile) {
				return@with false
			}
			
			val properties = file.properties?.topLevelPropertyList ?: return@with false
			val prop = properties.find {
				it.propertyKey == property
			} ?: return@with false
			
			if (value != null) return@with prop.propertyValue == value else return@with true
		}
	}
	
	class Capture<T : PsiElement> : AdvancedSearchElementPattern<T, Capture<T>> {
		constructor(aClass: Class<T>) : super(aClass)
		constructor(condition: InitialPatternCondition<T>) : super(condition)
	}
}

open class AdvancedSearchStringLiteralPattern<T : AdvancedSearchPsiStringLiteral, Self : AdvancedSearchStringLiteralPattern<T, Self>> : AdvancedSearchElementPattern<T, Self> {
	constructor(aClass: Class<T>) : super(aClass)
	constructor(condition: InitialPatternCondition<T>) : super(condition)
	
	fun withSingleStringSequence(ignoreEscapes: Boolean = false): Self {
		return with("AdvancedSearchStringLiteralPattern.withSingleStringSequence") { t, _ ->
			var count = 0
			t.isRawString || (t.node.children().all {
				when (it.elementType) {
					AdvancedSearchTypes.STRING_ESCAPE_SEQ -> ignoreEscapes
					AdvancedSearchTypes.STRING_SEQ -> ++count <= 1
					else -> true
				}
			} && count == 1)
		}
	}
	
	/**
	 * quotes won't be included
	 */
	fun thatDoesntContain(text: String): Self {
		return with("AdvancedSearchStringLiteralPattern.thatDoesntContain") { t, _ ->
			text !in t.stringText
		}
	}
	
	class Capture<T : AdvancedSearchPsiStringLiteral> : AdvancedSearchStringLiteralPattern<T, Capture<T>> {
		constructor(aClass: Class<T>) : super(aClass)
		constructor(condition: InitialPatternCondition<T>) : super(condition)
	}
}

class AdvancedSearchParameterPattern : AdvancedSearchElementPattern<AdvancedSearchPsiParameter, AdvancedSearchParameterPattern>(AdvancedSearchPsiParameter::class.java) {
	/**
	 * specify [text] with '$'
	 */
	fun withVariableText(text: String): AdvancedSearchParameterPattern {
		return with("AdvancedSearchParameterPattern.withVariableText") { t, _ ->
			val statement = t.parentOfType<AdvancedSearchPsiStatement>() ?: return@with false
			return@with statement.variable.text == text
		}
	}
	
	fun withIdentifierText(text: String): AdvancedSearchParameterPattern {
		return with("AdvancedSearchParameterPattern.withIdentifierText") { t, _ ->
			val statement = t.parentOfType<AdvancedSearchPsiStatement>() ?: return@with false
			return@with statement.identifier?.text == text
		}
	}
}
