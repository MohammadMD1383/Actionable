package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.lexer.FlexAdapter

class MacroTemplateLexerAdapter : FlexAdapter(MacroTemplateLexer(null))
