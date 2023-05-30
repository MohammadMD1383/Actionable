package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class AdvancedSearchFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, AdvancedSearchLanguage) {
	override fun getFileType() = AdvancedSearchFileType
	override fun toString() = "Advanced Search File"
}
