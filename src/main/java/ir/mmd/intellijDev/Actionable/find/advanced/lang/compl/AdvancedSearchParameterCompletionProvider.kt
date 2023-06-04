package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.statement

private fun createLookupElement(text: String): LookupElement {
	return LookupElementBuilder.create(text)
}

class AdvancedSearchParameterCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset)
		
		javaAccessModifiers(element, parameters, result)
	}
	
	private fun javaAccessModifiers(element: PsiElement?, parameters: CompletionParameters, result: CompletionResultSet) {
		val criteria = psiElement()
			.inside(statement()
				.withVariable("\$class", "\$type", "\$interface", "\$annotation", checkParent = true)
				.withIdentifier("has-modifier"))
			.withTopLevelProperty("language", "java")
		
		if (criteria.accepts(element)) {
			result.addElement(createLookupElement("public"))
			result.addElement(createLookupElement("private"))
			result.addElement(createLookupElement("protected"))
		}
	}
}
