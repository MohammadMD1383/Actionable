package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.Commenter

class AdvancedSearchCommenter : Commenter {
	override fun getLineCommentPrefix() = "#"
	override fun getBlockCommentPrefix() = null
	override fun getBlockCommentSuffix() = null
	override fun getCommentedBlockCommentPrefix() = null
	override fun getCommentedBlockCommentSuffix() = null
}
