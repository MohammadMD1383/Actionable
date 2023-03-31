package ir.mmd.intellijDev.Actionable.lang.xml.settings

import com.intellij.openapi.options.Configurable

class Settings : Configurable {
	override fun getDisplayName() = "XML"
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() = Unit
}
