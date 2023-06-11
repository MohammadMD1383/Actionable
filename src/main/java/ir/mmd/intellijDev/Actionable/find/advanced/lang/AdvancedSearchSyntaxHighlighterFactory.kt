package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes

class AdvancedSearchSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
	private class AdvancedSearchSyntaxHighlighter : SyntaxHighlighterBase() {
		override fun getHighlightingLexer() = AdvancedSearchLexerAdapter()
		
		override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
			return when (tokenType) {
				AdvancedSearchTypes.VARIABLE -> arrayOf(DefaultLanguageHighlighterColors.KEYWORD)
				AdvancedSearchTypes.IDENTIFIER -> arrayOf(DefaultLanguageHighlighterColors.NUMBER)
				AdvancedSearchTypes.STRING_ESCAPE_SEQ -> arrayOf(DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
				AdvancedSearchTypes.COMMENT -> arrayOf(DefaultLanguageHighlighterColors.LINE_COMMENT)
				AdvancedSearchTypes.COMMA -> arrayOf(DefaultLanguageHighlighterColors.COMMA)
				AdvancedSearchTypes.EOS -> arrayOf(DefaultLanguageHighlighterColors.SEMICOLON)
				
				AdvancedSearchTypes.STRING_SEQ,
				AdvancedSearchTypes.SINGLE_QUOTE,
				AdvancedSearchTypes.DOUBLE_QUOTE -> arrayOf(DefaultLanguageHighlighterColors.STRING)
				
				else -> TextAttributesKey.EMPTY_ARRAY
			}
		}
	}
	
	override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
		return AdvancedSearchSyntaxHighlighter()
	}
}
