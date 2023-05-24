package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.openapi.fileTypes.LanguageFileType

object MacroTemplateFileType : LanguageFileType(MacroTemplateLanguage) {
	override fun getName() = "Actionable Macro Template"
	override fun getDescription() = "Actionable macro template file"
	override fun getDefaultExtension() = "mt"
	override fun getIcon() = null
}
