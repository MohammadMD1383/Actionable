package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.find.advanced.lang.*

class AdvancedSearchCompletionContributor : CompletionContributor(), DumbAware {
	init {
		extend(
			CompletionType.BASIC,
			variable().inside(statement()),
			AdvancedSearchVariableCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			identifier().inside(statement()),
			AdvancedSearchIdentifierCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			stringSequence().inside(
				stringLiteral().withSingleStringSequence().thatDoesntContain(" ").inside(parameter())
			),
			AdvancedSearchParameterCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			identifier().inside(topLevelProperty()),
			AdvancedSearchTopLevelPropertyCompletionProvider()
		)
		
		extend(
			CompletionType.BASIC,
			stringSequence().inside(
				stringLiteral().withSingleStringSequence().thatDoesntContain(" ").inside(topLevelProperty())
			),
			AdvancedSearchTopLevelPropertyValueCompletionProvider()
		)
	}
	
	override fun beforeCompletion(context: CompletionInitializationContext) {
		context.dummyIdentifier = "dummy"
	}
}
