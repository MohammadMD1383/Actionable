package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findExtensionFor
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findLanguagePropertyValue
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType.Variable
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.findElementAtOrBefore
import ir.mmd.intellijDev.Actionable.util.ext.moveForward
import ir.mmd.intellijDev.Actionable.util.ext.moveTo

private val insertHandler = InsertHandler<LookupElement> { context, _ ->
	val editor = context.editor
	val caret = editor.caretModel.currentCaret
	val element = (context.file.elementAt(caret)
		?.parent as? AdvancedSearchPsiStatement)
		?.psiIdentifier
	
	if (element != null) {
		caret moveTo element.textOffset
	} else {
		context.document.insertString(caret.offset, " ")
		caret.moveForward()
	}
	
	AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
}

private fun CompletionResultSet.add(project: Project, str: String) {
	addElement(
		LookupElementBuilder.create(AdvancedSearchLightPsiElement(project, Variable, "$$str"))
			.bold().withIcon(AllIcons.Nodes.Type).withInsertHandler(insertHandler)
	)
}

class AdvancedSearchVariableCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAtOrBefore(parameters.offset) ?: return
		val ctx = AdvancedSearchContext(element)
		val language = element.findLanguagePropertyValue() ?: return
		val project = parameters.editor.project!!
		
		AdvancedSearchExtensionPoint.findExtensionFor(language)
			?.getCompletionProvider(project)
			?.getVariables(ctx)
			?.forEach {
				result.add(project, it)
			}
	}
}
