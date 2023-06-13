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
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findExtensionFor
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findLanguagePropertyValue
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType.Property
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiTopLevelProperty
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.moveForward
import ir.mmd.intellijDev.Actionable.util.ext.moveTo

private val insertHandler = InsertHandler<LookupElement> { context, _ ->
	val editor = context.editor
	val caret = editor.caretModel.currentCaret
	val element = context.file.elementAt(caret)
		?.parentOfType<AdvancedSearchPsiTopLevelProperty>()
	val value = element?.stringLiteral
	val colon = element?.colon
	val document = context.document
	
	when {
		value != null -> {
			caret moveTo value.textOffset + 1
		}
		
		colon != null -> {
			val offset = colon.textOffset
			document.insertString(offset + 1, " ''")
			caret moveTo offset + 3
		}
		
		else -> {
			document.insertString(caret.offset, ": ''")
			caret.moveForward(3)
		}
	}
	
	AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
}

private fun CompletionResultSet.add(project: Project, str: String) {
	addElement(
		LookupElementBuilder.create(AdvancedSearchLightPsiElement(project, Property, str))
			.bold().withIcon(AllIcons.Nodes.Property)
			.withInsertHandler(insertHandler)
	)
}

class AdvancedSearchTopLevelPropertyCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val project = parameters.editor.project!!
		result.add(project, "language")
		
		val language = parameters.originalFile.findLanguagePropertyValue() ?: return
		AdvancedSearchExtensionPoint.findExtensionFor(language)
			?.getCompletionProvider(project)
			?.getTopLevelProperties()
			?.forEach {
				result.add(project, it)
			}
	}
}
