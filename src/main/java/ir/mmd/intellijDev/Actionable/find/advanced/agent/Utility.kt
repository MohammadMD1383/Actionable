package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.util.after

sealed interface Condition {
	fun evaluate(): Boolean
	
	class StringCondition internal constructor(private val initial: String?) : Condition {
		private var value = true
		override fun evaluate() = value
		
		infix fun equalTo(other: String) = this after { value = initial == other }
		infix fun or(other: String) = this after { value = value || initial == other }
	}
	
	class ListCondition<T> internal constructor(private val initial: List<T>) : Condition {
		private var value = true
		override fun evaluate() = value
		
		infix fun contains(other: T) = this after { value = other in initial }
		infix fun or(other: T) = this after { value = value || other in initial }
	}
}

class AdvancedSearchContextData internal constructor(
	val variable: String?,
	val identifier: String?,
	val parameters: List<String>
) {
	inner class Criteria internal constructor() {
		val variable = Condition.StringCondition(this@AdvancedSearchContextData.variable)
		val identifier = Condition.StringCondition(this@AdvancedSearchContextData.identifier)
		val parameters = Condition.ListCondition(this@AdvancedSearchContextData.parameters)
		
		fun evaluate() = variable.evaluate() and identifier.evaluate()
	}
}

operator fun AdvancedSearchContextData?.invoke(block: context(AdvancedSearchContextData.Criteria) () -> Unit): Boolean {
	this ?: return false
	val c = Criteria()
	block(c)
	return c.evaluate()
}

class AdvancedSearchContext internal constructor(element: PsiElement) {
	private val parents: List<AdvancedSearchContextData>
	
	init {
		val mParents = mutableListOf<AdvancedSearchContextData>()
		
		var e = element.parentOfType<AdvancedSearchPsiStatement>()
		while (e != null) {
			mParents.add(AdvancedSearchContextData(e.variable, e.identifier, e.parameters.map { it.stringLiteral.content }))
			e = e.parentOfType<AdvancedSearchPsiStatement>()
		}
		
		parents = mParents
	}
	
	operator fun get(level: Int): AdvancedSearchContextData? {
		return parents.getOrNull(level)
	}
}
