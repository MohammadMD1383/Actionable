package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatementBody

class AdvancedSearchFoldingBuilder : FoldingBuilderEx(), DumbAware {
	override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
		return PsiTreeUtil.collectElementsOfType(root, AdvancedSearchPsiStatementBody::class.java).map {
			val range = it.textRange
			FoldingDescriptor(it.node, TextRange(range.startOffset + 1, range.endOffset - 1))
		}.toTypedArray()
	}
	
	override fun getPlaceholderText(node: ASTNode) = "..."
	override fun isCollapsedByDefault(node: ASTNode) = false
}
