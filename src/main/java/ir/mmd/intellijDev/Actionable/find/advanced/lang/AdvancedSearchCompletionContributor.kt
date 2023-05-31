package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.psiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiTopLevelProperty
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes
import ir.mmd.intellijDev.Actionable.util.ext.moveForward

class AdvancedSearchCompletionContributor : CompletionContributor() {
	init {
		extend(
			CompletionType.BASIC,
			psiElement(AdvancedSearchTypes.VARIABLE).inside(AdvancedSearchPsiStatement::class.java),
			AdvancedSearchVariableCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			psiElement(AdvancedSearchTypes.IDENTIFIER).inside(AdvancedSearchPsiStatement::class.java),
			AdvancedSearchIdentifierCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			psiElement(AdvancedSearchTypes.IDENTIFIER).inside(AdvancedSearchPsiTopLevelProperty::class.java),
			AdvancedSearchTopLevelPropertyCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			psiElement(AdvancedSearchTypes.VALUE).inside(AdvancedSearchPsiTopLevelProperty::class.java),
			AdvancedSearchTopLevelPropertyValueCompletionProvider()
		)
	}
	
	override fun beforeCompletion(context: CompletionInitializationContext) {
		context.dummyIdentifier = "dummy"
	}
	
	private class AdvancedSearchVariableCompletionProvider : CompletionProvider<CompletionParameters>() {
		private companion object {
			private val insertHandler = InsertHandler<LookupElement> { context, _ ->
				val caret = context.editor.caretModel.currentCaret
				context.document.insertString(caret.offset, " ")
				caret.moveForward()
			}
			
			private fun createLookupElement(str: String): LookupElement {
				return LookupElementBuilder.create("$$str").bold().withIcon(AllIcons.Nodes.Type)
					.withInsertHandler(insertHandler)
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			// todo: specific to context and parent elements
			result.addAllElements(listOf(
				createLookupElement("class"),
				createLookupElement("method"),
				createLookupElement("interface"),
				createLookupElement("annotation")
			))
		}
	}
	
	private class AdvancedSearchIdentifierCompletionProvider : CompletionProvider<CompletionParameters>() {
		private companion object {
			private val insertHandler = InsertHandler<LookupElement> { context, _ ->
				val caret = context.editor.caretModel.currentCaret
				context.document.insertString(caret.offset, " ")
				caret.moveForward()
			} // todo: merge whitespace inserted
			// todo: make global insert handler
			
			private fun createLookupElement(str: String): LookupElement {
				return LookupElementBuilder.create(str).bold().withIcon(AllIcons.Nodes.Variable)
					.withInsertHandler(insertHandler)
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			// todo: specific to variable provided before
			result.addAllElements(listOf(
				createLookupElement("extends"),
				createLookupElement("implements"),
				createLookupElement("has-method"),
				createLookupElement("has-param"),
				createLookupElement("super-of")
			))
		}
	}
	
	private class AdvancedSearchTopLevelPropertyCompletionProvider : CompletionProvider<CompletionParameters>() {
		private companion object {
			private val insertHandler = InsertHandler<LookupElement> { context, _ ->
				val caret = context.editor.caretModel.currentCaret
				context.document.insertString(caret.offset, ": ")
				caret.moveForward(2)
			}
			
			private fun createLookupElement(str: String): LookupElement {
				return LookupElementBuilder.create(str).bold().withIcon(AllIcons.Nodes.Property)
					.withInsertHandler(insertHandler)
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			result.addAllElements(listOf(
				createLookupElement("language")
			))
		}
	}
	
	private class AdvancedSearchTopLevelPropertyValueCompletionProvider : CompletionProvider<CompletionParameters>() {
		private companion object {
			private fun createLookupElement(str: String): LookupElement {
				return LookupElementBuilder.create(str)
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			result.addAllElements(listOf(
				createLookupElement("java"),
				createLookupElement("kotlin")
			))
		}
	}
}
