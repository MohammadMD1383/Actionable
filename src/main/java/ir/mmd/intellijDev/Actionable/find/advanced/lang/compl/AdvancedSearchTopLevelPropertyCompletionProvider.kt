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
import ir.mmd.intellijDev.Actionable.util.ext.moveForward

private val insertHandler = InsertHandler<LookupElement> { context, _ ->
	val editor = context.editor
	val caret = editor.caretModel.currentCaret
	context.document.insertString(caret.offset, ": ''")
	caret.moveForward(3)
	AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
}

private fun createLookupElement(str: String): LookupElement {
	return LookupElementBuilder.create(str).bold().withIcon(AllIcons.Nodes.Property)
		.withInsertHandler(insertHandler)
}

class AdvancedSearchTopLevelPropertyCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		result.addElement(createLookupElement("language"))
		result.addElement(createLookupElement("scope"))
		result.addElement(createLookupElement("scan-source"))
	}
}
