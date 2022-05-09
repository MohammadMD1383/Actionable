package ir.mmd.intellijDev.Actionable.settings

import com.intellij.openapi.options.Configurable

/**
 * Main setting entry for the plugin
 *
 * provides no default ui
 */
class Settings : Configurable {
	override fun getDisplayName() = "Actionable"
	override fun getHelpTopic() = null
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() {}
}
