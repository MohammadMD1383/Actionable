package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.statement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.statementBody
import ir.mmd.intellijDev.Actionable.util.ext.moveForward

private val insertHandler = InsertHandler<LookupElement> { context, _ ->
	val editor = context.editor
	val caret = editor.caretModel.currentCaret
	context.document.insertString(caret.offset, " ")
	caret.moveForward()
	AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
}

private fun createLookupElement(str: String): LookupElement {
	return LookupElementBuilder.create("$$str").bold().withIcon(AllIcons.Nodes.Type)
		.withInsertHandler(insertHandler)
}

class AdvancedSearchVariableCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset).let {
			if (it?.elementType == AdvancedSearchTypes.VARIABLE) it else {
				parameters.originalFile.findElementAt(parameters.offset - 1)
			}
		}
		
		javaTopLevelVariables(element, result)
		javaInsideTypeVariables(element, result)
	}
	
	private fun javaInsideTypeVariables(element: PsiElement?, result: CompletionResultSet) {
		val criteria = psiElement()
			.withSuperParent(2, statementBody().withParent(statement()
				.withVariable("\$class", "\$interface", "\$type", "\$annotation")))
			.withTopLevelProperty("language", "java")
		
		if (criteria.accepts(element)) {
			result.addElement(createLookupElement("method"))
		}
	}
	
	private fun javaTopLevelVariables(element: PsiElement?, result: CompletionResultSet) {
		val criteria = psiElement()
			.withParent(statement()
				.withoutParentStatement())
			.withTopLevelProperty("language", "java")
		
		if (criteria.accepts(element)) {
			result.addElement(createLookupElement("type"))
			result.addElement(createLookupElement("class"))
			result.addElement(createLookupElement("method"))
			result.addElement(createLookupElement("interface"))
			result.addElement(createLookupElement("annotation"))
		}
	}
}
