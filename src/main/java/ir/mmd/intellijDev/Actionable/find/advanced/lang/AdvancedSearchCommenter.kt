package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.codeInsight.generation.CommenterDataHolder
import com.intellij.codeInsight.generation.SelfManagingCommenter
import com.intellij.lang.Commenter
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.ext.getLineStartIndentLength
import ir.mmd.intellijDev.Actionable.util.ext.getLineText
import kotlin.math.min

@Suppress("CompanionObjectInExtension")
class AdvancedSearchCommenter : Commenter, SelfManagingCommenter<CommenterDataHolder> {
	companion object {
		private val LMC_KEY = Key<Int>("AAS.LEFT_MOST_COLUMN")
	}
	
	// <editor-fold desc="unused methods">
	override fun getBlockCommentPrefix() = null
	override fun getBlockCommentSuffix() = null
	override fun getCommentedBlockCommentPrefix() = null
	override fun getCommentedBlockCommentSuffix() = null
	override fun createBlockCommentingState(selectionStart: Int, selectionEnd: Int, document: Document, file: PsiFile) = null
	override fun insertBlockComment(startOffset: Int, endOffset: Int, document: Document?, data: CommenterDataHolder?) = null
	override fun uncommentBlockComment(startOffset: Int, endOffset: Int, document: Document?, data: CommenterDataHolder?) = Unit
	override fun getBlockCommentSuffix(selectionEnd: Int, document: Document, data: CommenterDataHolder) = null
	override fun getBlockCommentPrefix(selectionStart: Int, document: Document, data: CommenterDataHolder) = null
	override fun getBlockCommentRange(selectionStart: Int, selectionEnd: Int, document: Document, data: CommenterDataHolder) = null
	// </editor-fold>
	
	override fun getLineCommentPrefix() = "#"
	override fun getCommentPrefix(line: Int, document: Document, data: CommenterDataHolder) = "#"
	
	override fun createLineCommentingState(startLine: Int, endLine: Int, document: Document, file: PsiFile): CommenterDataHolder {
		return object : CommenterDataHolder() {}.apply {
			var leftMostCol = Int.MAX_VALUE
			
			for (line in startLine..endLine) {
				val text = document.getLineText(line)
				if (text.isEmpty()) {
					continue
				}
				
				leftMostCol = min(leftMostCol, text.takeWhile { it in " " }.length)
			}
			
			if (leftMostCol == Int.MAX_VALUE) {
				leftMostCol = 0
			}
			
			putUserData(LMC_KEY, leftMostCol)
		}
	}
	
	override fun isLineCommented(line: Int, offset: Int, document: Document, data: CommenterDataHolder): Boolean {
		return document.getLineText(line).trim().startsWith('#')
	}
	
	override fun commentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
		val leftMostCol = data.getUserData(LMC_KEY) ?: return
		var col = document.getLineStartIndentLength(line)
		var commentText = "# "
		
		if (leftMostCol > col) {
			commentText = " ".repeat(leftMostCol - col) + commentText
		} else {
			col = leftMostCol
		}
		
		document.insertString(document.getLineStartOffset(line) + col, commentText)
	}
	
	override fun uncommentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
		val lineText = document.getLineText(line)
		val lineStart = document.getLineStartOffset(line)
		val removeStart = lineStart + lineText.takeWhile { it in " " }.length
		val removeEnd = lineStart + lineText.run {
			val sharp = indexOf('#')
			if (getOrNull(sharp + 1) == ' ') sharp + 1 else sharp
		}
		
		document.deleteString(removeStart, removeEnd + 1)
	}
}
