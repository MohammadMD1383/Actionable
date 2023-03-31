package ir.mmd.intellijDev.Actionable.lang.java.settings

import com.intellij.openapi.options.Configurable

class Settings : Configurable {
	override fun getDisplayName() = "Java"
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() = Unit
}
