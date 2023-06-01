package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.PsiTreeUtil
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatements
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiTopLevelProperties

class AdvancedSearchFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, AdvancedSearchLanguage) {
	override fun getFileType() = AdvancedSearchFileType
	override fun toString() = "Advanced Search File"
	
	val properties: AdvancedSearchPsiTopLevelProperties?
		get() = PsiTreeUtil.getChildOfType(this, AdvancedSearchPsiTopLevelProperties::class.java)
	
	val statements: AdvancedSearchPsiStatements?
		get() = PsiTreeUtil.getChildOfType(this, AdvancedSearchPsiStatements::class.java)
}
