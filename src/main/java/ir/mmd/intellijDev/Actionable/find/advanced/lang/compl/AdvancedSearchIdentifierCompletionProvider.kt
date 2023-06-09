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
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType.Identifier
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.moveForward
import ir.mmd.intellijDev.Actionable.util.ext.moveTo

private val insertHandler = InsertHandler<LookupElement> { context, _ ->
	val editor = context.editor
	val caret = editor.caretModel.currentCaret
	val param = (context.file.elementAt(caret)
		?.parent as? AdvancedSearchPsiStatement)
		?.psiParameters?.parameterList?.firstOrNull()
	
	if (param != null) {
		caret moveTo param.textRange.startOffset + 1
	} else {
		context.document.insertString(caret.offset, " ''")
		caret.moveForward(2)
	}
	
	AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
}

private fun CompletionResultSet.add(project: Project, str: String, withInsertHandler: Boolean) {
	addElement(
		LookupElementBuilder.create(AdvancedSearchLightPsiElement(project, Identifier, str))
			.bold().withIcon(AllIcons.Nodes.Variable)
			.withInsertHandler(if (withInsertHandler) insertHandler else null)
	)
}

class AdvancedSearchIdentifierCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val project = parameters.editor.project!!
		val element = parameters.originalFile.findElementAt(parameters.offset) ?: return
		
		val parents = mutableListOf<String>()
		var e = element.parentOfType<AdvancedSearchPsiStatement>()
		val variable = e!!.variable
		e = e.parentOfType<AdvancedSearchPsiStatement>()
		while (e != null) {
			parents.add(e.variable ?: e.identifier ?: "")
			e = e.parentOfType<AdvancedSearchPsiStatement>()
		}
		
		val language = (element.containingFile as AdvancedSearchFile).properties?.languagePsiProperty?.value ?: return
		AdvancedSearchExtensionPoint.extensionList.find { it.language.equals(language, ignoreCase = true) }
			?.completionProviderInstance
			?.getIdentifiers(project, variable, parents)
			?.forEach {
				result.add(project, it.first, it.second)
			}
	}
}
