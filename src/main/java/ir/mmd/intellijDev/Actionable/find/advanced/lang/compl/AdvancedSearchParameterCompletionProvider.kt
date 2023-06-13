package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findExtensionFor
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findLanguagePropertyValue
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType.Parameter

private fun CompletionResultSet.add(project: Project, str: String) {
	addElement(LookupElementBuilder.create(AdvancedSearchLightPsiElement(project, Parameter, str)))
}

class AdvancedSearchParameterCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset) ?: return
		val language = element.findLanguagePropertyValue() ?: return
		val project = parameters.editor.project!!
		val ctx = AdvancedSearchContext(element)
		
		AdvancedSearchExtensionPoint.findExtensionFor(language)
			?.getCompletionProvider(project)
			?.getParameters(ctx)
			?.forEach {
				result.add(project, it)
			}
	}
}
