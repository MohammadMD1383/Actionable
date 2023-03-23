package ir.mmd.intellijDev.Actionable.caret.settings

import com.intellij.openapi.options.Configurable

/**
 * Settings [Configurable] UI for `Actionable > Caret`
 */
class Settings : Configurable {
	override fun getDisplayName() = "Caret"
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() {}
}
