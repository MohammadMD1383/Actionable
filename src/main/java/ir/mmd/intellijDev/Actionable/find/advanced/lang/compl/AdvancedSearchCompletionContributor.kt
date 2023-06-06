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
		// for top-level property keys
		extend(
			CompletionType.BASIC,
			identifier().inside(topLevelProperty()),
			AdvancedSearchTopLevelPropertyCompletionProvider()
		)
		
		// for top-level property values
		extend(
			CompletionType.BASIC,
			stringSequence().inside(
				stringLiteral().withSingleStringSequence().thatDoesntContain(" ").inside(parameter())
			),
			AdvancedSearchParameterCompletionProvider()
		)
		
		// for $variable inside statements
		extend(
			CompletionType.BASIC,
			variable().inside(statement()),
			AdvancedSearchVariableCompletionProvider()
		)
		
		// for identifier inside statements
		extend(
			CompletionType.BASIC,
			identifier().inside(statement()),
			AdvancedSearchIdentifierCompletionProvider()
		)
		
		// for statement parameters
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
