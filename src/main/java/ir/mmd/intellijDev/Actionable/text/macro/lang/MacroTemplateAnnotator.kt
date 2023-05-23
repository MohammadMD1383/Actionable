package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiPlaceholder

class MacroTemplateAnnotator : Annotator, DumbAware {
	override fun annotate(element: PsiElement, holder: AnnotationHolder) {
		if (element is MacroTemplatePsiPlaceholder) {
			if (element.placeholderName !in listOf("SELECTION", "ELEMENT", "WORD")) { // todo
				holder.newAnnotation(HighlightSeverity.ERROR, "Unknown placeholder")
					.range(element)
					.highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
					.withFix(RemoveUnknownPlaceholderQuickFix(element))
					.create()
				return
			}
			
			holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
				.range(element)
				.textAttributes(DefaultLanguageHighlighterColors.KEYWORD)
				.create()
		}
	}
	
	private class RemoveUnknownPlaceholderQuickFix(private val placeholder:MacroTemplatePsiPlaceholder) : BaseIntentionAction() {
		override fun getFamilyName() = "Remove unknown placeholder"
		override fun getText() = "Remove `${placeholder.placeholderName}`"
		override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true
		override fun invoke(project: Project, editor: Editor?, file: PsiFile?) = placeholder.delete()
	}
}
