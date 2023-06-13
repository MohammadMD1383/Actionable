package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.project.Project
import ir.mmd.intellijDev.Actionable.find.advanced.agent.*
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile

class JavaAdvancedSearchExtensionFactory : AdvancedSearchExtensionFactory() {
	override fun createAgent(project: Project, file: AdvancedSearchFile): AdvancedSearchAgent {
		return JavaAdvancedSearchAgent(project, file)
	}
	
	override fun getCompletionProvider(project: Project): AdvancedSearchCompletionProvider {
		return JavaAdvancedSearchCompletionProvider
	}
	
	override fun getAnnotator(project: Project): Annotator {
		return JavaAdvancedSearchAnnotator
	}
	
	override fun getDocumentationProvider(project: Project): AdvancedSearchDocumentationProvider {
		return JavaAdvancedSearchDocumentationProvider
	}
	
	override fun getInjectionProvider(project: Project): AdvancedSearchInjectionProvider {
		return JavaAdvancedSearchInjectionProvider
	}
}
