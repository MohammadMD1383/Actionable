package ir.mmd.intellijDev.Actionable.caret.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class Settings : Configurable {
	override fun getDisplayName() = "Caret"
	override fun createComponent(): JComponent? = null
	override fun isModified() = false
	override fun apply() {}
}
