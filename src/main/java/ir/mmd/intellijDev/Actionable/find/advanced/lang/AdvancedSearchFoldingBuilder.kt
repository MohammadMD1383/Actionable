package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatementBody
import ir.mmd.intellijDev.Actionable.util.ext.innerSubRange

class AdvancedSearchFoldingBuilder : FoldingBuilderEx(), DumbAware {
	override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
		return PsiTreeUtil.collectElementsOfType(root, AdvancedSearchPsiStatementBody::class.java).filter {
			it.rbrace != null && it.text != "{}"
		}.map {
			FoldingDescriptor(it.node, it.textRange.innerSubRange(1, 1))
		}.toTypedArray()
	}
	
	override fun getPlaceholderText(node: ASTNode) = "..."
	override fun isCollapsedByDefault(node: ASTNode) = false
}
