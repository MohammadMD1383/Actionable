package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.DumbAware
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateElementPattern.Companion.psiElement
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiPlaceholder
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes
import ir.mmd.intellijDev.Actionable.text.macro.macroPlaceholderNames
import ir.mmd.intellijDev.Actionable.util.ext.moveForward

class MacroTemplatePlaceholderCompletionContributor : CompletionContributor(), DumbAware {
	init {
		extend(
			CompletionType.BASIC,
			psiElement(MacroTemplateTypes.PLACEHOLDER_NAME).inside(MacroTemplatePsiPlaceholder::class.java),
			MacroTemplatePlaceholderCompletionProvider()
		)
	}
	
	override fun beforeCompletion(context: CompletionInitializationContext) {
		context.dummyIdentifier = "PLACEHOLDER"
		context.offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, context.identifierEndOffset - 1)
	}
	
	private class MacroTemplatePlaceholderCompletionProvider : CompletionProvider<CompletionParameters>() {
		companion object {
			private val placeholderInsertHandler = InsertHandler<LookupElement> { context, _ ->
				context.editor.caretModel.currentCaret.moveForward()
			}
		}
		
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			macroPlaceholderNames.forEach {
				result.addElement(
					LookupElementBuilder.create(it).bold().withIcon(AllIcons.Nodes.Type)
						.withTypeText("Placeholder", true)
						.withInsertHandler(placeholderInsertHandler)
				)
			}
		}
	}
}
