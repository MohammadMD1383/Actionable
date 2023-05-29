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
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiCaretIndicator
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiFactory
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiPlaceholder
import ir.mmd.intellijDev.Actionable.text.macro.macroPlaceholderNames

class MacroTemplateAnnotator : Annotator, DumbAware {
	override fun annotate(element: PsiElement, holder: AnnotationHolder) {
		when (element) {
			is MacroTemplatePsiPlaceholder -> {
				if (element.placeholderName.text !in macroPlaceholderNames) {
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
			
			is MacroTemplatePsiCaretIndicator -> {
				if (element.numberInt != 0) {
					holder.newAnnotation(HighlightSeverity.ERROR, "Caret indicators other than 0 is not supported yet")
						.range(element)
						.highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
						.withFix(ReplaceCaretIndicatorWithZeroQuickFix(element))
						.create()
					return
				}
				
				holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
					.range(element)
					.textAttributes(DefaultLanguageHighlighterColors.STRING)
					.create()
			}
		}
	}
	
	private class RemoveUnknownPlaceholderQuickFix(private val placeholder: MacroTemplatePsiPlaceholder) : BaseIntentionAction() {
		override fun getFamilyName() = "Remove unknown placeholder"
		override fun getText() = "Remove `${placeholder.placeholderName.text}`"
		override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true
		override fun invoke(project: Project, editor: Editor?, file: PsiFile?) = placeholder.delete()
	}
	
	private class ReplaceCaretIndicatorWithZeroQuickFix(private val caretIndicator: MacroTemplatePsiCaretIndicator) : BaseIntentionAction() {
		override fun getFamilyName() = "Change unsupported caret indicator number to `0`"
		override fun getText() = "Replace `${caretIndicator.numberInt}` with `0`"
		override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true
		
		override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
			caretIndicator.number.replace(MacroTemplatePsiFactory.createNumberFromInt(project, 0))
		}
	}
}
