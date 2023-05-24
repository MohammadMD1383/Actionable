package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes

class MacroTemplateSyntaxHighlighter : SyntaxHighlighterBase() {
	override fun getHighlightingLexer(): MacroTemplateLexerAdapter = MacroTemplateLexerAdapter()
	override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = when (tokenType) {
		MacroTemplateTypes.ESCAPED_DOLLAR,
		MacroTemplateTypes.ESCAPED_ESCAPE -> arrayOf(DefaultLanguageHighlighterColors.NUMBER)
		
		else -> emptyArray()
	}
}
