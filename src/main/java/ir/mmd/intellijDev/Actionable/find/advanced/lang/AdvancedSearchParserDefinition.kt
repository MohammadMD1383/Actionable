package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes

class AdvancedSearchParserDefinition : ParserDefinition {
	companion object {
		val FILE = IFileElementType(AdvancedSearchLanguage)
	}
	
	override fun createLexer(project: Project): Lexer = AdvancedSearchLexerAdapter()
	override fun createParser(project: Project): PsiParser = AdvancedSearchParser()
	override fun getFileNodeType(): IFileElementType = FILE
	override fun getCommentTokens(): TokenSet = TokenSet.EMPTY
	override fun getStringLiteralElements(): TokenSet = TokenSet.create(AdvancedSearchTypes.STRING_SEQ)
	override fun createElement(node: ASTNode?): PsiElement = AdvancedSearchTypes.Factory.createElement(node)
	override fun createFile(viewProvider: FileViewProvider): PsiFile = AdvancedSearchFile(viewProvider)
}
