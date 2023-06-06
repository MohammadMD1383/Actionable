package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.topLevelProperty
import javax.swing.Icon

private fun createLookupElement(str: String, icon: Icon? = null): LookupElement {
	return LookupElementBuilder.create(str).withIcon(icon)
}

class AdvancedSearchTopLevelPropertyValueCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset)
		
		languageValues(element, result)
		scopeValues(element, result)
		scanSourceValues(element, result)
	}
	
	private fun scanSourceValues(element: PsiElement?, result: CompletionResultSet) {
		val criteria = psiElement()
			.inside(topLevelProperty()
				.withKey("scan-source"))
		
		if (criteria.accepts(element)) {
			result.addElement(createLookupElement("true"))
			result.addElement(createLookupElement("false"))
		}
	}
	
	private fun scopeValues(element: PsiElement?, result: CompletionResultSet) {
		val criteria = psiElement()
			.inside(topLevelProperty()
				.withKey("scope"))
		
		if (criteria.accepts(element)) {
			result.addElement(createLookupElement("all"))
			result.addElement(createLookupElement("project"))
		}
	}
	
	private fun languageValues(element: PsiElement?, result: CompletionResultSet) {
		val criteria = psiElement()
			.inside(topLevelProperty()
				.withKey("language"))
		
		if (criteria.accepts(element)) {
			result.addAllElements(Language.getRegisteredLanguages().mapNotNull {
				if (it.id.isEmpty()) null else createLookupElement(it.id, it.associatedFileType?.icon)
			})
		}
	}
}
