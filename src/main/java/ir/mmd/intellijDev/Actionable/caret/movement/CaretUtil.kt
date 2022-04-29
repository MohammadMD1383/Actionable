package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.util.contains
import ir.mmd.intellijDev.Actionable.util.isPositive

/**
 * This is a wrapper class over [Caret] for convenient movement
 */
class CaretUtil(private val caret: Caret) {
	companion object {
		/**
		 * move caret forward
		 */
		const val FORWARD = 1
		
		/**
		 * move caret backward
		 */
		const val BACKWARD = -1
	}
	
	private val document = caret.editor.document
	
	/**
	 * temporary caret offset
	 */
	var offset = caret.offset
	
	inline val nextChar: Char? get() = peek(+1)
	inline val prevChar: Char? get() = peek(-1)
	
	/**
	 * changes back the [CaretUtil.offset] to the **last [CaretUtil.commit]** and adds the given offset to it.
	 *
	 * @param offset the offset to be applied to the [CaretUtil.offset] after resetting it
	 */
	fun reset(offset: Int = 0) {
		this.offset = caret.offset + offset
	}
	
	/**
	 * moves the [caret] to the [CaretUtil.offset]
	 *
	 * @param offset the offset to be added to [CaretUtil.offset] before committing
	 */
	fun commit(offset: Int = 0) = caret.moveToOffset(offset.let { this.offset += it; this.offset })
	
	/**
	 * peeks and returns the character at the given offset from current [CaretUtil.offset]
	 *
	 * @param offset the offset from current **temporary caret position**
	 * @return the character or null if the evaluated offset is invalid in the parent [Document] of the [Caret]
	 */
	fun peek(offset: Int): Char? = if (offset == 0) null else document.charsSequence.getOrNull(this.offset + offset.run {
		this + if (isPositive xor caret.logicalPosition.leansForward) 0 else if (isPositive) -1 else +1
	}) // todo inspection: convert with() to run() and vice versa
	
	/**
	 * moves the temporary [CaretUtil.offset]
	 *
	 * @param offset can be either positive or negative
	 */
	fun move(offset: Int) {
		this.offset += offset
	}
	
	fun moveWhileFacing(chars: String, hardStops: String, step: Int) {
		while (true) if ((peek(step) ?: break).let { it in hardStops || it !in chars }) break else move(step)
	}
	
	fun moveUntilReach(chars: String, hardStops: String, step: Int) {
		while (peek(step) !in (chars + hardStops)) move(step)
	}
}
