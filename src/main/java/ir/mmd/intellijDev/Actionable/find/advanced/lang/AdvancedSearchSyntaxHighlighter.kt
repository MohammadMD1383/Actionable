package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes

class AdvancedSearchSyntaxHighlighter : SyntaxHighlighterBase() {
	override fun getHighlightingLexer() = AdvancedSearchLexerAdapter()
	
	override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
		return when (tokenType) {
			AdvancedSearchTypes.VARIABLE -> arrayOf(DefaultLanguageHighlighterColors.KEYWORD)
			AdvancedSearchTypes.IDENTIFIER -> arrayOf(DefaultLanguageHighlighterColors.NUMBER)
			else -> TextAttributesKey.EMPTY_ARRAY
		}
	}
}
