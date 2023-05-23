package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes

class MacroTemplatePlaceholderCompletionContributor : CompletionContributor() {
	init {
		extend(
			CompletionType.BASIC,
			psiElement().afterLeaf(psiElement(MacroTemplateTypes.DOLLAR)).beforeLeaf(psiElement(MacroTemplateTypes.DOLLAR)),
			MacroTemplatePlaceholderCompletionProvider()
		)
	}
	
	private class MacroTemplatePlaceholderCompletionProvider : CompletionProvider<CompletionParameters>() {
		override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
			result.addAllElements(
				listOf( // todo
					LookupElementBuilder.create("SELECTION").bold().withIcon(AllIcons.Nodes.Type),
					LookupElementBuilder.create("ELEMENT").bold().withIcon(AllIcons.Nodes.Type),
					LookupElementBuilder.create("WORD").bold().withIcon(AllIcons.Nodes.Type),
				)
			)
		}
	}
}
