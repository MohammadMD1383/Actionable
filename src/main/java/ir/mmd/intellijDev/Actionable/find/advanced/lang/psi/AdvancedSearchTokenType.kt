package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi

import com.intellij.psi.tree.IElementType
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchLanguage
import org.jetbrains.annotations.NonNls

class AdvancedSearchTokenType(debugName: @NonNls String) : IElementType(debugName, AdvancedSearchLanguage) {
	override fun toString() = "AdvancedSearchTokenType.${super.toString()}"
}
