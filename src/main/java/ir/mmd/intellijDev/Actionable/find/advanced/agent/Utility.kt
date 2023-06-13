package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.compl.AdvancedSearchIdentifierCompletionProvider
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatementBody
import ir.mmd.intellijDev.Actionable.util.after

sealed interface Condition {
	fun evaluate(): Boolean
	
	class StringCondition internal constructor(private val initial: String?) : Condition {
		private var value = true
		override fun evaluate() = value
		
		infix fun equalTo(other: String) = this after { value = initial == other }
		infix fun or(other: String) = this after { value = value || initial == other }
		infix fun matches(pattern: Regex) = this after { value = initial?.matches(pattern) ?: false }
	}
	
	class ListCondition<T> internal constructor(private val initial: List<T>) : Condition {
		private var value = true
		override fun evaluate() = value
		
		infix fun contains(other: T) = this after { value = other in initial }
		infix fun or(other: T) = this after { value = value || other in initial }
		infix fun and(other: T) = this after { value = value && other in initial }
	}
}

/**
 * see [AdvancedSearchContext]
 */
class AdvancedSearchContextData internal constructor(
	val variable: String?,
	val identifier: String?,
	val parameters: List<String>
) {
	inner class Criteria internal constructor() {
		val variable = Condition.StringCondition(this@AdvancedSearchContextData.variable)
		val identifier = Condition.StringCondition(this@AdvancedSearchContextData.identifier)
		val parameters = Condition.ListCondition(this@AdvancedSearchContextData.parameters)
		
		fun evaluate() = variable.evaluate() and identifier.evaluate() and parameters.evaluate()
	}
}

operator fun AdvancedSearchContextData?.invoke(block: context(AdvancedSearchContextData.Criteria) () -> Unit): Boolean {
	this ?: return false
	val c = Criteria()
	block(c)
	return c.evaluate()
}

operator fun List<AdvancedSearchContextData>.invoke(block: context(AdvancedSearchContextData.Criteria) () -> Unit): Boolean {
	return any { it(block) }
}

/**
 * **this context demonstrates where an operation happened in the advanced search file's hierarchy.**
 *
 * * `context[0]` stands for the current statement if there is one.
 *
 * * `context[1]...context[n]` stand for parent statements if there are some.
 *
 * * to check if the operation had happened at top-level in the file
 * (meaning that this is the top-most statement), use [atTopLevel].
 *
 * to check for a criteria see below:
 *
 * * check if we have `$class` or `$interface` at current statement:
 *     ```kotlin
 *     context[0] { variable equalTo "\$class" or "\$interface" }
 *     ```
 *
 * * check if we have `$method` at this statement or the parent and
 * the identifier of this statement is `name-matches`:
 *     ```kotlin
 *     context[0..1] { variable equalTo "\$method" } and context[0] { identifier equalTo "name-matches" }
 *     ```
 *
 * * to check parameters:
 *     ```kotlin
 *     context[n] { parameters contains ... or ... }
 *     context[n] { parameters contains ... and ... }
 *     ```
 *
 * * **NOTICE:** such expression is not supported and would result in an undefined behavior:
 *     ```kotlin
 *     context[n] { parameters contains (... and ...) or (... and ...) }
 *     ```
 *
 * for examples please explore the [ir.mmd.intellijDev.Actionable.find.advanced.agent.java] package.
 */
class AdvancedSearchContext internal constructor(element: PsiElement) {
	private val parents: List<AdvancedSearchContextData>
	
	init {
		parents = mutableListOf<AdvancedSearchContextData>().apply {
			var e: AdvancedSearchPsiStatement? = element.parentOfType<AdvancedSearchPsiStatement>()
			if (e == null) {
				// handle top-level context
				add(AdvancedSearchContextData(null, null, listOf()))
				return@apply
			}
			
			element.parentOfType<AdvancedSearchPsiStatementBody>()?.let {
				// in order to handle an empty context inside statement body
				// $class extends 'SomeClass' { | }
				// where (|) is caret
				if (it.textRange in e!!.textRange) {
					add(AdvancedSearchContextData(null, null, listOf()))
				}
			}
			
			val b = element.getUserData(AdvancedSearchIdentifierCompletionProvider.DUMMY_IDENTIFIER) != true
			add(AdvancedSearchContextData(e.variable, if (b) e.identifier else null, e.parameters.map { it.stringLiteral.content }))
			
			e = e.parentOfType<AdvancedSearchPsiStatement>()
			while (e != null) {
				add(AdvancedSearchContextData(e.variable, e.identifier, e.parameters.map { it.stringLiteral.content }))
				e = e.parentOfType<AdvancedSearchPsiStatement>()
			}
		}
	}
	
	operator fun get(level: Int): AdvancedSearchContextData? {
		return parents.getOrNull(level)
	}
	
	operator fun get(levelRange: IntRange): List<AdvancedSearchContextData> {
		if (parents.isEmpty()) {
			return emptyList()
		}
		
		val start = if (levelRange.first < 0) 0 else levelRange.first
		val end = if (levelRange.last > parents.lastIndex) parents.size else levelRange.last + 1
		return parents.subList(start, end)
	}
	
	val atTopLevel: Boolean get() = parents.size == 1
}

fun PsiElement.findLanguagePropertyValue(): String? {
	val file = (containingFile as? AdvancedSearchFile) ?: return null
	return file.properties?.languagePsiProperty?.value
}

fun ExtensionPointName<AdvancedSearchProviderFactoryBean>.findExtensionFor(language: String): AdvancedSearchExtensionFactory? {
	return extensionList.find { it.language.equals(language, ignoreCase = true) }?.factoryClass
}
