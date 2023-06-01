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
	
	/**
	 * don't specify quotes
	 */
	@JvmStatic
	fun createRawStringFromText(project: Project, text: String): AdvancedSearchPsiRawString {
		return createFileFromText(project, "a:'$text'")
			.properties!!.topLevelPropertyList[0]
			.stringLiteral as AdvancedSearchPsiRawString
	}
	
	/**
	 * don't specify quotes
	 */
	@JvmStatic
	fun createStringFromText(project: Project, text: String): AdvancedSearchPsiString {
		return createFileFromText(project, "a:\"$text\"")
			.properties!!.topLevelPropertyList[0]
			.stringLiteral as AdvancedSearchPsiString
	}
	
	/**
	 * specify [text] without quotes
	 */
	@JvmStatic
	fun createParameterFromText(project: Project, text: String): AdvancedSearchPsiParameter {
		return createFileFromText(project, "a:'b';\$c d '$text'")
			.statements!!.statementList[0]
			.parameters!!.parameterList[0]
	}
}
