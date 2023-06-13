package ir.mmd.intellijDev.Actionable.text.macro.lang.psi.impl

import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiCaretIndicator

fun getNumberInt(element: MacroTemplatePsiCaretIndicator) = element.number.text.toInt()
