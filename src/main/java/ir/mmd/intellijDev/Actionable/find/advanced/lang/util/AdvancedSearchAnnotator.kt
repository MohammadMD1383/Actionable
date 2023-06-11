package ir.mmd.intellijDev.Actionable.find.advanced.lang.util

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findExtensionFor
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findLanguagePropertyValue

class AdvancedSearchAnnotator : Annotator {
	override fun annotate(element: PsiElement, holder: AnnotationHolder) {
		annotateDefaults(element, holder)
		
		val language = element.findLanguagePropertyValue() ?: return
		AdvancedSearchExtensionPoint.findExtensionFor(language)
			?.getAnnotator(element.project)
			?.annotate(element, holder)
	}
	
	private fun annotateDefaults(element: PsiElement, holder: AnnotationHolder) {}
}
