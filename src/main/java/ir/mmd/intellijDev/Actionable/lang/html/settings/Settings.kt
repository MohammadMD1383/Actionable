package ir.mmd.intellijDev.Actionable.lang.html.settings

import com.intellij.openapi.options.Configurable

/**
 * Settings [Configurable] UI for `Actionable > Language Specific > HTML`
 */
class Settings : Configurable {
	override fun getDisplayName() = "HTML"
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() = Unit
}
