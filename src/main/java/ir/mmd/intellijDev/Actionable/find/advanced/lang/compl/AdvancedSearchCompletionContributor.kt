package ir.mmd.intellijDev.Actionable.find.advanced.lang.compl

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.util.elementType
import ir.mmd.intellijDev.Actionable.find.advanced.lang.*
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes
import ir.mmd.intellijDev.Actionable.util.ext.elementAt

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
		
		val element = context.file.elementAt(context.caret)
		if (element?.elementType == AdvancedSearchTypes.IDENTIFIER ||
			element?.elementType == AdvancedSearchTypes.VARIABLE) {
			context.offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, element!!.textRange.endOffset)
		}
	}
}
