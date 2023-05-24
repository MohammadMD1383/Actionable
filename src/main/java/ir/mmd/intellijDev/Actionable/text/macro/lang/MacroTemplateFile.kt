package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class MacroTemplateFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, MacroTemplateLanguage) {
	override fun getFileType() = MacroTemplateFileType
	override fun toString() = "Macro Template File"
}
