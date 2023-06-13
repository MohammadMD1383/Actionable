package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.light.LightElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchLanguage

class AdvancedSearchLightPsiElement(project: Project, val type: ElementType, private val value: String)
	: LightElement(PsiManager.getInstance(project), AdvancedSearchLanguage) {
	enum class ElementType {
		/**
		 * top-level property key
		 */
		Property,
		
		/**
		 * top-level property value
		 */
		Value,
		
		/**
		 * $variable
		 */
		Variable,
		
		/**
		 * identifier
		 */
		Identifier,
		
		/**
		 * statement parameter
		 */
		Parameter
	}
	
	override fun toString() = value
}
