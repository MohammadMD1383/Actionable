package ir.mmd.intellijDev.Actionable.settings

import com.intellij.openapi.options.Configurable


class Settings : Configurable {
	override fun getDisplayName() = "Actionable"
	override fun getHelpTopic() = null
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() {}
}
