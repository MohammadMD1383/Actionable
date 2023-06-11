package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.patterns.InitialPatternCondition
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.*
import org.jetbrains.kotlin.psi.psiUtil.children

fun psiElement() = AdvancedSearchElementPattern.Capture(PsiElement::class.java)
fun <T : PsiElement> psiElement(aClass: Class<T>) = AdvancedSearchElementPattern.Capture(aClass)
fun psiElement(type: IElementType) = psiElement().withElementType(type)
fun variable() = psiElement(AdvancedSearchTypes.VARIABLE)
fun identifier() = psiElement(AdvancedSearchTypes.IDENTIFIER)
fun statement() = AdvancedSearchStatementPattern()
fun statementBody() = psiElement(AdvancedSearchTypes.STATEMENT_BODY)
fun stringLiteral() = AdvancedSearchStringLiteralPattern.Capture(AdvancedSearchPsiStringLiteral::class.java)
fun stringSequence() = psiElement(AdvancedSearchTypes.STRING_SEQ)
fun parameter() = AdvancedSearchParameterPattern()
fun topLevelProperty() = AdvancedSearchTopLevelPropertyPattern()

open class AdvancedSearchElementPattern<T : PsiElement, Self : AdvancedSearchElementPattern<T, Self>> : PsiElementPattern<T, Self> {
	constructor(aClass: Class<T>) : super(aClass)
	constructor(condition: InitialPatternCondition<T>) : super(condition)
	
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
	 *
	 * @param ignoreCase whether to compare [property] and [value] ignoring the case or not
	 */
	fun withTopLevelProperty(property: String, value: String? = null, ignoreCase: Boolean = true): Self {
		return with("AdvancedSearchElementPattern.withTopLevelProperty") { t, _ ->
			val file = t.containingFile
			if (file !is AdvancedSearchFile) {
				return@with false
			}
			
			val prop = file.properties?.findPsiPropertyByKey(property, ignoreCase) ?: return@with false
			if (value != null) return@with prop.value.equals(value, ignoreCase) else return@with true
		}
	}
	
	fun withoutParentStatement() = with("AdvancedSearchElementPattern.withoutParentStatement") { t, _ ->
		t.parentOfType<AdvancedSearchPsiStatement>() == null
	}
	
	fun insideFirstStatement() = with("insideFirstStatement") { t, _ ->
		val statement = t.parentOfType<AdvancedSearchPsiStatement>() ?: return@with false
		val statements = statement.parentOfType<AdvancedSearchPsiStatements>() ?: return@with false
		statements.statementList.first() == statement
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
			t.isRaw || (t.node.children().all {
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
			text !in t.content
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
	fun withVariable(vararg text: String): AdvancedSearchParameterPattern {
		return with("AdvancedSearchParameterPattern.withVariable") { t, _ ->
			val v = t.parentOfType<AdvancedSearchPsiStatement>()?.identifier ?: return@with false
			text.any { v == it }
		}
	}
	
	fun withIdentifier(vararg text: String): AdvancedSearchParameterPattern {
		return with("AdvancedSearchParameterPattern.withIdentifier") { t, _ ->
			val i = t.parentOfType<AdvancedSearchPsiStatement>()?.identifier ?: return@with false
			text.any { i == it }
		}
	}
	
	fun withIdentifier(pattern: Regex): AdvancedSearchParameterPattern {
		return with("AdvancedSearchParameterPattern.withIdentifier(regex)") { t, _ ->
			val identifier = t.parentOfType<AdvancedSearchPsiStatement>()?.psiIdentifier?.text ?: return@with false
			pattern.matches(identifier)
		}
	}
}

class AdvancedSearchTopLevelPropertyPattern : AdvancedSearchElementPattern<AdvancedSearchPsiTopLevelProperty, AdvancedSearchTopLevelPropertyPattern>(AdvancedSearchPsiTopLevelProperty::class.java) {
	fun withKey(k: String): AdvancedSearchTopLevelPropertyPattern {
		return with("AdvancedSearchTopLevelPropertyPattern.withKey") { t, _ ->
			t.key == k
		}
	}
	
	fun withValue(v: String): AdvancedSearchTopLevelPropertyPattern {
		return with("AdvancedSearchTopLevelPropertyPattern.withValue") { t, _ ->
			t.value == v
		}
	}
}

class AdvancedSearchStatementPattern : AdvancedSearchElementPattern<AdvancedSearchPsiStatement, AdvancedSearchStatementPattern>(AdvancedSearchPsiStatement::class.java) {
	fun withVariable(vararg text: String, checkParent: Boolean = false) = with("AdvancedSearchStatementPattern.withVariable") { t, _ ->
		val v = t.variable ?: (if (checkParent) t.parentStatement?.variable else null) ?: return@with false
		text.any { v == it }
	}
	
	fun withIdentifier(vararg text: String) = with("AdvancedSearchStatementPattern.withIdentifier") { t, _ ->
		val i = t.identifier ?: return@with false
		text.any { i == it }
	}
}
