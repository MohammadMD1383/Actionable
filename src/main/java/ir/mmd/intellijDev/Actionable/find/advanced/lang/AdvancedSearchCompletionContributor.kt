package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.Language
import com.intellij.openapi.project.DumbAware
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.identifier
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.psiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.stringLiteral
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.stringSequence
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.topLevelProperty
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchElementPattern.Companion.variable
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiTopLevelProperty
import ir.mmd.intellijDev.Actionable.util.ext.moveForward
import javax.swing.Icon

class AdvancedSearchCompletionContributor : CompletionContributor(), DumbAware {
	init {
		extend(
			CompletionType.BASIC,
			variable().inside<AdvancedSearchPsiStatement>(),
			AdvancedSearchVariableCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			identifier().inside<AdvancedSearchPsiStatement>(),
			AdvancedSearchIdentifierCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			identifier().inside<AdvancedSearchPsiTopLevelProperty>(),
			AdvancedSearchTopLevelPropertyCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			stringSequence().inside(stringLiteral().withSingleStringSequence().thatDoesntContain(" ").inside<AdvancedSearchPsiTopLevelProperty>()),
			AdvancedSearchTopLevelPropertyValueCompletionProvider()
		)
	}
	
	override fun beforeCompletion(context: CompletionInitializationContext) {
		context.dummyIdentifier = "dummy"
	}
	
	private class AdvancedSearchVariableCompletionProvider : CompletionProvider<CompletionParameters>() {
		private companion object {
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
				val editor = context.editor
				val caret = editor.caretModel.currentCaret
				context.document.insertString(caret.offset, " ''")
				caret.moveForward(2)
				AutoPopupController.getInstance(context.project).autoPopupMemberLookup(editor, null)
			} // todo: merge whitespace inserted
			// todo: make global insert handler
			
			private fun createLookupElement(str: String, skipInsertHandler: Boolean = false): LookupElement {
				val builder = LookupElementBuilder.create(str).bold().withIcon(AllIcons.Nodes.Variable)
				return if (skipInsertHandler) builder else builder.withInsertHandler(insertHandler)
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			// todo: specific to variable provided before
			result.addAllElements(listOf(
				createLookupElement("extends"),
				createLookupElement("extends-directly"),
				createLookupElement("implements-directly"),
				createLookupElement("has-modifier"),
				createLookupElement("has-method"),
				createLookupElement("has-method-directly"),
				createLookupElement("has-param"),
				createLookupElement("super-of"),
				createLookupElement("name-matches"),
				createLookupElement("is-anonymous", true),
			))
		}
	}
	
	private class AdvancedSearchTopLevelPropertyCompletionProvider : CompletionProvider<CompletionParameters>() {
		private companion object {
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
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			result.addElement(createLookupElement("language"))
			result.addElement(createLookupElement("scope"))
		}
	}
	
	private class AdvancedSearchTopLevelPropertyValueCompletionProvider : CompletionProvider<CompletionParameters>() {
		private companion object {
			private fun createLookupElement(str: String, icon: Icon? = null): LookupElement {
				return LookupElementBuilder.create(str).withIcon(icon)
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			val element = parameters.originalFile.findElementAt(parameters.offset)
			
			if (psiElement().inside(topLevelProperty().withKey("language")).accepts(element)) {
				result.addAllElements(Language.getRegisteredLanguages().mapNotNull {
					if (it.id.isEmpty()) null else createLookupElement(it.id, it.associatedFileType?.icon)
				})
			}
			
			if (psiElement().inside(topLevelProperty().withKey("scope")).accepts(element)) {
				result.addElement(createLookupElement("all"))
				result.addElement(createLookupElement("project"))
			}
		}
	}
}
