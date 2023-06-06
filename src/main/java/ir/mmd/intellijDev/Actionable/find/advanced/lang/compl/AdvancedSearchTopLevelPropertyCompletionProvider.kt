package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.util.ext.moveForward

private val insertHandler = InsertHandler<LookupElement> { ctx, _ ->
	val editor = ctx.editor
	val caret = editor.caretModel.currentCaret
	ctx.document.insertString(caret.offset, ": ''")
	caret.moveForward(3)
	AutoPopupController.getInstance(ctx.project).autoPopupMemberLookup(editor, null)
}// todo: merge whitespace

private fun CompletionResultSet.add(s: String) {
	addElement(
		LookupElementBuilder.create(s)
			.bold().withIcon(AllIcons.Nodes.Property)
			.withInsertHandler(insertHandler)
	)
}

class AdvancedSearchTopLevelPropertyCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		result.add("language")
		
		(parameters.originalFile as AdvancedSearchFile).properties?.languagePsiProperty?.value?.let { language ->
			AdvancedSearchExtensionPoint.extensionList.find {
				it.language.equals(language, ignoreCase = true)
			}?.completionProviderInstance?.getTopLevelProperties(parameters.editor.project!!)?.forEach {
				result.add(it)
			}
		}
	}
}
