package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes

class MacroTemplateParserDefinition : ParserDefinition {
	companion object {
		val FILE = IFileElementType(MacroTemplateLanguage)
	}
	
	override fun createLexer(project: Project): MacroTemplateLexerAdapter = MacroTemplateLexerAdapter()
	override fun createParser(project: Project): MacroTemplateParser = MacroTemplateParser()
	override fun getFileNodeType(): IFileElementType = FILE
	override fun getCommentTokens(): TokenSet = TokenSet.EMPTY
	override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY
	override fun createElement(node: ASTNode?): PsiElement = MacroTemplateTypes.Factory.createElement(node)
	override fun createFile(viewProvider: FileViewProvider): MacroTemplateFile = MacroTemplateFile(viewProvider)
}
