package ir.mmd.intellijDev.Actionable.lang.settings

import com.intellij.openapi.options.Configurable

/**
 * Settings [Configurable] UI for `Actionable > Language Specific`
 */
class Settings : Configurable {
	override fun getDisplayName() = "Language Specific"
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() = Unit
}
