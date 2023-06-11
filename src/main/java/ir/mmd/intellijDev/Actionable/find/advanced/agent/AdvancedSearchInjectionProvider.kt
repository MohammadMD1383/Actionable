package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.lang.Language

abstract class AdvancedSearchInjectionProvider {
	class InjectionDescriptor(
		val language: Language,
		val prefix: String? = null,
		val suffix: String? = null
	)
	
	abstract fun getInjectionFor(context: AdvancedSearchContext): InjectionDescriptor?
}
