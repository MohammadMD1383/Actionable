package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType.Value
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiTopLevelProperties
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiTopLevelProperty
import javax.swing.Icon

private fun CompletionResultSet.add(project: Project, str: String, icon: Icon? = null) {
	addElement(
		LookupElementBuilder.create(AdvancedSearchLightPsiElement(project, Value, str))
			.withIcon(icon)
	)
}

class AdvancedSearchTopLevelPropertyValueCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset) ?: return
		val property = element.parentOfType<AdvancedSearchPsiTopLevelProperty>()?.key ?: return
		val project = parameters.editor.project!!
		val extensionList = AdvancedSearchExtensionPoint.extensionList
		
		if (property.equals("language", ignoreCase = true)) {
			extensionList.forEach { ext ->
				val language = Language.getRegisteredLanguages().find { ext.language.equals(it.id, ignoreCase = true) }
				if (language != null) {
					result.add(project, language.id, language.associatedFileType?.icon)
				}
			}
			
			return
		}
		
		val language = element.parentOfType<AdvancedSearchPsiTopLevelProperties>()?.languagePsiProperty?.value ?: return
		extensionList.find { it.language.equals(language, ignoreCase = true) }
			?.completionProviderInstance
			?.getValuesForProperty(project, property)
			?.forEach {
				result.add(project, it)
			}
	}
}
