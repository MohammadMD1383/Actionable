package ir.mmd.intellijDev.Actionable.text.macro.lang.psi

import com.intellij.psi.tree.IElementType
import ir.mmd.intellijDev.Actionable.text.macro.lang.MacroTemplateLanguage
import org.jetbrains.annotations.NonNls

class MacroTemplateTokenType(debugName: @NonNls String) : IElementType(debugName, MacroTemplateLanguage) {
	override fun toString() = "MacroTemplateTokenType.${super.toString()}"
}
