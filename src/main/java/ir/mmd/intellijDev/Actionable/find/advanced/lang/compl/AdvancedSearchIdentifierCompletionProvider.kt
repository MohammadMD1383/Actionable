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
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiFactory
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.statement
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
		AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
	}
} // todo: merge whitespace inserted

private fun createLookupElement(str: String, skipInsertHandler: Boolean = false): LookupElement {
	val builder = LookupElementBuilder.create(str).bold().withIcon(AllIcons.Nodes.Variable)
	return if (skipInsertHandler) builder else builder.withInsertHandler(insertHandler)
}

class AdvancedSearchIdentifierCompletionProvider : CompletionProvider<CompletionParameters>() {
	override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
		val element = parameters.originalFile.findElementAt(parameters.offset).let {
			if (it?.elementType == AdvancedSearchTypes.IDENTIFIER) it else {
				AdvancedSearchPsiFactory.createFileFromText(
					parameters.editor.project!!,
					parameters.editor.document.text
						.replaceRange(parameters.offset, parameters.offset, "dummy")
				).findElementAt(parameters.offset)
			}
		}
		
		javaIdentifiersForClassInterfaceType(element, result)
		javaIdentifiersForMethod(element, result)
	}
	
	private fun javaIdentifiersForMethod(element: PsiElement?, result: CompletionResultSet) {
		val criteria = commonCriteria(
			"\$method",
			language = "java",
			checkParent = true
		)
		
		if (criteria.accepts(element)) {
			result.addElement(createLookupElement("has-param"))
			result.addElement(createLookupElement("name-matches"))
		}
	}
	
	private fun javaIdentifiersForClassInterfaceType(element: PsiElement?, result: CompletionResultSet) {
		val criteria = commonCriteria(
			"\$class", "\$interface", "\$type",
			language = "java",
			checkParent = true
		)
		
		if (criteria.accepts(element)) {
			result.addElement(createLookupElement("super-of"))
			result.addElement(createLookupElement("extends"))
			result.addElement(createLookupElement("implements"))
			result.addElement(createLookupElement("extends-directly"))
			result.addElement(createLookupElement("implements-directly"))
			result.addElement(createLookupElement("has-modifier"))
			result.addElement(createLookupElement("has-method"))
			result.addElement(createLookupElement("has-method-directly"))
			result.addElement(createLookupElement("name-matches"))
			result.addElement(createLookupElement("is-anonymous", true))
		}
	}
	
	private fun commonCriteria(vararg variable: String, language: String, checkParent: Boolean): AdvancedSearchElementPattern.Capture<PsiElement> {
		return psiElement()
			.withParent(statement()
				.withVariable(*variable, checkParent = checkParent))
			.withTopLevelProperty("language", language)
	}
}
