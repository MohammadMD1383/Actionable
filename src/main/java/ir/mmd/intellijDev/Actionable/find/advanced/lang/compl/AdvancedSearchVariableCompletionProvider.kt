package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
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
		AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
	}
}

private fun CompletionResultSet.add(str: String) {
	addElement(
		LookupElementBuilder.create("$$str").bold().withIcon(AllIcons.Nodes.Type)
			.withInsertHandler(insertHandler)
	)
}

class AdvancedSearchVariableCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset).let {
			if (it?.elementType == AdvancedSearchTypes.VARIABLE) it else {
				parameters.originalFile.findElementAt(parameters.offset - 1)
			}
		} ?: return
		
		val parents = mutableListOf<String>()
		var e = element.parentOfType<AdvancedSearchPsiStatement>()?.parentOfType<AdvancedSearchPsiStatement>()
		while (e != null) {
			parents.add(e.variable ?: e.identifier ?: "")
			e = e.parentOfType<AdvancedSearchPsiStatement>()
		}
		
		val language = (element.containingFile as AdvancedSearchFile).properties?.languagePsiProperty?.value ?: return
		AdvancedSearchExtensionPoint.extensionList.find { it.language.equals(language, ignoreCase = true) }
			?.completionProviderInstance
			?.getVariables(parameters.editor.project!!, parents)
			?.forEach {
				result.add(it)
			}
	}
}
