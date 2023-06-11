package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.lang.Language

interface AdvancedSearchInjectionProvider {
	class InjectionDescriptor(
		val language: Language,
		val prefix: String? = null,
		val suffix: String? = null
	)
	
	fun getInjectionFor(context: AdvancedSearchContext): InjectionDescriptor?
}
