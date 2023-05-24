package ir.mmd.intellijDev.Actionable.text.macro.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.Disposer
import ir.mmd.intellijDev.Actionable.text.macro.reRegisterMacros
import javax.swing.JComponent

/**
 * Settings [Configurable] UI for `Actionable > Text > Macro`
 */
class Settings : Configurable {
	private var ui: UI? = null
	
	override fun getDisplayName() = "Macro"
	override fun createComponent(): JComponent = ui?.component ?: UI().run { ui = this; component }
	override fun isModified() = true
	override fun reset() = Unit
	
	override fun apply() {
		ui!!.saveChanges()
		reRegisterMacros()
	}
	
	override fun disposeUIResources() {
		ui?.let { Disposer.dispose(it) }
		ui = null
	}
}
