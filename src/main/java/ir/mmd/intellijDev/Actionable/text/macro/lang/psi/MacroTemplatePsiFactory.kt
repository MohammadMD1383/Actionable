package ir.mmd.intellijDev.Actionable.text.macro.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.childrenOfType
import com.intellij.util.IncorrectOperationException
import ir.mmd.intellijDev.Actionable.text.macro.lang.MacroTemplateFile
import ir.mmd.intellijDev.Actionable.text.macro.lang.MacroTemplateLanguage

object MacroTemplatePsiFactory {
	@JvmStatic
	fun createFileFromText(project: Project, text: String): MacroTemplateFile {
		return PsiFileFactory.getInstance(project).createFileFromText("FactoryFile", MacroTemplateLanguage, text) as MacroTemplateFile
	}
	
	@JvmStatic
	fun createPlaceholderFromText(project: Project, text: String): MacroTemplatePsiPlaceholder {
		if (text.isEmpty() || !text.all { it in 'A'..'Z' || it == '_' }) {
			throw IncorrectOperationException("placeholder must match: /[A-Z_]+/")
		}
		
		return createFileFromText(project, "\$$text\$").childrenOfType<MacroTemplatePsiPlaceholder>().single()
	}
	
	@JvmStatic
	fun createPlaceholderNameFromText(project: Project, text: String): PsiElement {
		return createPlaceholderFromText(project, text).placeholderName
	}
	
	@JvmStatic
	fun createCaretIndicatorFromInt(project: Project, number: Int): MacroTemplatePsiCaretIndicator {
		return createFileFromText(project, "\$$number\$").childrenOfType<MacroTemplatePsiCaretIndicator>().single()
	}
	
	@JvmStatic
	fun createNumberFromInt(project: Project, number: Int): PsiElement {
		return createCaretIndicatorFromInt(project, number).number
	}
}
