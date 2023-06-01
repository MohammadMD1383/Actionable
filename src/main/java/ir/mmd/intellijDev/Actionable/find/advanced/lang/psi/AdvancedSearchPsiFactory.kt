package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchLanguage

object AdvancedSearchPsiFactory {
	@JvmStatic
	fun createFileFromText(project: Project, text: String): AdvancedSearchFile {
		return PsiFileFactory.getInstance(project).createFileFromText(AdvancedSearchLanguage, text) as AdvancedSearchFile
	}
	
	fun createParameterFromText(project: Project, text: String): AdvancedSearchPsiParameter {
		val file = createFileFromText(project, "a:b\n\n\$c d $text")
		return file.statements!!.statementList[0].parameters!!.parameterList[0]
	}
}
