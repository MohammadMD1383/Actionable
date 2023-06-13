package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType

object AdvancedSearchFileType : LanguageFileType(AdvancedSearchLanguage) {
	override fun getName() = "Advanced Search"
	override fun getDescription() = "Actionable advanced search file"
	override fun getDefaultExtension() = "aas"
	override fun getIcon() = AllIcons.Actions.Search
}
