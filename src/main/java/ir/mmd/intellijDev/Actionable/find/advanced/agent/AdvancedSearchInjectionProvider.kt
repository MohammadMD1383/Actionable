package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.lang.Language
import com.intellij.openapi.project.Project

interface AdvancedSearchInjectionProvider {
	class InjectionDescriptor(
		val language: Language,
		val prefix: String? = null,
		val suffix: String? = null
	)
	
	fun getInjectionFor(project: Project, variable: String?, identifier: String, context: List<String>): InjectionDescriptor?
}
