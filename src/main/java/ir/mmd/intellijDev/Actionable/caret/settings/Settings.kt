package ir.mmd.intellijDev.Actionable.caret.settings

import com.intellij.openapi.options.Configurable
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep

@Keep
class Settings : Configurable {
	override fun getDisplayName() = "Caret"
	override fun getHelpTopic() = null
	override fun createComponent() = null
	override fun isModified() = false
	override fun apply() {}
}
