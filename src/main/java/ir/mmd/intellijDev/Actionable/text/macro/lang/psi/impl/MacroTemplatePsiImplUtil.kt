package ir.mmd.intellijDev.Actionable.text.macro.lang.psi.impl

import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiCaretIndicator

object MacroTemplatePsiImplUtil {
	@JvmStatic
	fun getNumberInt(element: MacroTemplatePsiCaretIndicator) = element.number.text.toInt()
}
