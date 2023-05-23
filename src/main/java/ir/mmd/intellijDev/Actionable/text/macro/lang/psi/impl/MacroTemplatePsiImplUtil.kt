package ir.mmd.intellijDev.Actionable.text.macro.lang.psi.impl

import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiPlaceholder
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes

object MacroTemplatePsiImplUtil {
	@JvmStatic
	fun getPlaceholderName(placeholder: MacroTemplatePsiPlaceholder): String {
		return placeholder.node.findChildByType(MacroTemplateTypes.PLACEHOLDER_NAME)!!.text
	}
}
