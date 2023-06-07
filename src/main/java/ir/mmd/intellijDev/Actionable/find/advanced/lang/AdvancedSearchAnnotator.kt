package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint

class AdvancedSearchAnnotator : Annotator {
	override fun annotate(element: PsiElement, holder: AnnotationHolder) {
		annotateDefaults(element, holder)
		
		val language = (element.containingFile as AdvancedSearchFile).properties?.languagePsiProperty?.value ?: return
		AdvancedSearchExtensionPoint.extensionList.find { it.language.equals(language, ignoreCase = true) }
			?.annotatorInstance
			?.annotate(element, holder)
	}
	
	private fun annotateDefaults(element: PsiElement, holder: AnnotationHolder) {}
}
