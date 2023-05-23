package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class MacroTemplateSyntaxHighlighter : SyntaxHighlighterBase() {
	override fun getHighlightingLexer(): MacroTemplateLexerAdapter = MacroTemplateLexerAdapter()
	override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = emptyArray()
}
