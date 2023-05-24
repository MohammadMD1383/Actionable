package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.DumbAware
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateElementPattern.Companion.psiElement
import ir.mmd.intellijDev.Actionable.util.ext.moveForward

class MacroTemplatePlaceholderCompletionContributor : CompletionContributor(), DumbAware {
	init {
		extend(CompletionType.BASIC, psiElement().betweenDollars(), MacroTemplatePlaceholderCompletionProvider())
	}
	
	override fun beforeCompletion(context: CompletionInitializationContext) {
		context.dummyIdentifier = ""
		context.offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, context.editor.caretModel.currentCaret.offset)
	}
	
	private class MacroTemplatePlaceholderCompletionProvider : CompletionProvider<CompletionParameters>() {
		companion object {
			private val placeholderInsertHandler = InsertHandler<LookupElement> { context, _ ->
				context.editor.caretModel.currentCaret.moveForward()
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			result.addAllElements(
				listOf(
					// todo
					LookupElementBuilder.create("SELECTION").bold()
						.withIcon(AllIcons.Nodes.Type).withTypeText("Placeholder", true)
						.withInsertHandler(placeholderInsertHandler),
					
					LookupElementBuilder.create("ELEMENT").bold()
						.withIcon(AllIcons.Nodes.Type).withTypeText("Placeholder", true)
						.withInsertHandler(placeholderInsertHandler),
					
					LookupElementBuilder.create("WORD").bold()
						.withIcon(AllIcons.Nodes.Type).withTypeText("Placeholder", true)
						.withInsertHandler(placeholderInsertHandler),
				)
			)
		}
	}
}
