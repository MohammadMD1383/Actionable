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
import com.intellij.openapi.util.Key
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findExtensionFor
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findLanguagePropertyValue
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType.Identifier
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiFactory
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.findElementAtOrBefore
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
	companion object {
		@JvmField
		val DUMMY_IDENTIFIER = Key<Boolean>("AAS.DUMMY_IDENTIFIER")
	}
	
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val project = parameters.editor.project!!
		val offset = parameters.offset
		val language = parameters.originalFile.findLanguagePropertyValue() ?: return
		val element = (parameters.originalFile.findElementAtOrBefore(offset) ?: return).let {
			if (it.elementType == AdvancedSearchTypes.IDENTIFIER) it else {
				AdvancedSearchPsiFactory.createFileFromText(
					project, parameters.editor.document.text.replaceRange(offset, offset, "dummy")
				).findElementAt(offset)!!.also { e -> e.putUserData(DUMMY_IDENTIFIER, true) }
			}
		}
		val ctx = AdvancedSearchContext(element)
		
		AdvancedSearchExtensionPoint.findExtensionFor(language)
			?.getCompletionProvider(project)
			?.getIdentifiers(ctx)
			?.forEach {
				result.add(project, it.first, it.second)
			}
	}
}
