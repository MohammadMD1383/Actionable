package ir.mmd.intellijDev.Actionable.find.advanced.lang.fmt

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.*
import org.jetbrains.kotlin.psi.psiUtil.children

open class AdvancedSearchBlock(
	node: ASTNode, wrap: Wrap?, alignment: Alignment?,
	private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {
	companion object {
		val leafElTypes = arrayOf(
			STRING,
			RAW_STRING,
			PARAMETER
		)
	}
	
	private val sharedAlignment: Alignment by lazy { Alignment.createAlignment() }
	private val sharedNormalWrap: Wrap by lazy { Wrap.createWrap(WrapType.NORMAL, false) }
	
	private fun ASTNode.shouldSkipped() = (elementType == TokenType.WHITE_SPACE) || (elementType == EOS && text == "\n")
	private fun ASTNode.isIndented() = isStatements() && treeParent?.isStatementBody() ?: false
	private fun ASTNode.isLeaf() = elementType in leafElTypes
	private fun ASTNode.isStatement() = elementType == STATEMENT
	private fun ASTNode.isStatementBody() = elementType == STATEMENT_BODY
	private fun ASTNode.isStatements() = elementType == STATEMENTS
	private fun ASTNode.isParameters() = elementType == PARAMETERS
	private fun ASTNode.isParameter() = elementType == PARAMETER
	
	override fun buildChildren(): MutableList<Block> {
		val myList = mutableListOf<Block>()
		if (myNode.isLeaf()) {
			return myList
		}
		
		myNode.children().forEach {
			var childAlignment: Alignment? = null
			var childWrap: Wrap? = null
			
			when {
				it.shouldSkipped() -> return@forEach
				
				myNode.isParameters() and it.isParameter() -> {
					childAlignment = sharedAlignment
					childWrap = sharedNormalWrap
				}
				
				myNode.isStatements() and it.isStatement() -> {
					childAlignment = sharedAlignment
					childWrap = sharedNormalWrap
				}
			}
			
			myList.add(AdvancedSearchBlock(it, childWrap, childAlignment, spacingBuilder))
		}
		
		return myList
	}
	
	override fun getChildIndent(): Indent? {
		return if (myNode.isStatementBody()) Indent.getNormalIndent() else Indent.getNoneIndent()
	}
	
	override fun getIndent(): Indent? {
		return if (myNode.isIndented()) Indent.getNormalIndent() else Indent.getNoneIndent()
	}
	
	override fun getSpacing(child1: Block?, child2: Block): Spacing? {
		return spacingBuilder.getSpacing(this, child1, child2)
	}
	
	override fun isLeaf(): Boolean {
		return myNode.isLeaf() || myNode.firstChildNode == null
	}
}
