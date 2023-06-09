package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType.Parameter
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement

private fun CompletionResultSet.add(project: Project, str: String) {
	addElement(LookupElementBuilder.create(AdvancedSearchLightPsiElement(project, Parameter, str)))
}

class AdvancedSearchParameterCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset) ?: return
		val parents = mutableListOf<String>()
		var e = element.parentOfType<AdvancedSearchPsiStatement>()
		val variable = e!!.variable
		val identifier = e.identifier!!
		e = e.parentOfType<AdvancedSearchPsiStatement>()
		while (e != null) {
			parents.add(e.variable ?: e.identifier ?: "")
			e = e.parentOfType<AdvancedSearchPsiStatement>()
		}
		
		val language = (element.containingFile as AdvancedSearchFile).properties?.languagePsiProperty?.value
		val project = parameters.editor.project!!
		AdvancedSearchExtensionPoint.extensionList.find { it.language.equals(language, ignoreCase = true) }
			?.completionProviderInstance
			?.getParameters(project, variable, identifier, parents)
			?.forEach {
				result.add(project, it)
			}
	}
}
