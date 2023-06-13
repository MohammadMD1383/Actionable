package ir.mmd.intellijDev.Actionable.find.advanced.lang.fmt

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchLanguage
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.*

@Suppress("CompanionObjectInExtension")
class AdvancedSearchFormattingModelBuilder : FormattingModelBuilder {
	companion object {
		private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
			return SpacingBuilder(settings, AdvancedSearchLanguage)
				.before(EOS).none()
				.after(EOS).spacing(1, 0, 0, true, 1)
				.before(TOP_LEVEL_PROPERTY).spacing(0, 0, 0, true, 0)
				.after(TOP_LEVEL_PROPERTIES).spacing(0, 0, 2, true, 0)
				.around(STATEMENT).spacing(0, 0, 0, true, 1)
				.after(LBRACE).spacing(1, 0, 0, true, 0)
				.before(RBRACE).spacing(1, 0, 0, true, 0)
				.before(COLON).none()
				.after(COLON).spaces(1)
				.after(VARIABLE).spaces(1)
				.afterInside(IDENTIFIER, STATEMENT).spaces(1)
				.after(PARAMETERS).spaces(1)
				.before(COMMA).none()
				.after(COMMA).spaces(1)
		}
	}
	
	override fun createModel(formattingContext: FormattingContext): FormattingModel {
		val settings = formattingContext.codeStyleSettings
		return FormattingModelProvider.createFormattingModelForPsiFile(
			formattingContext.containingFile,
			AdvancedSearchBlock(
				formattingContext.node,
				null,
				null,
				createSpacingBuilder(settings)
			),
			settings
		)
	}
}
