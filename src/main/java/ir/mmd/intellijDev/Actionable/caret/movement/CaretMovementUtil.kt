package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document

/**
 * This is a wrapper class over [Caret] for convenient movement
 */
class CaretMovementUtil(private val caret: Caret) {
	private val document = caret.editor.document
	
	/**
	 * temporary caret offset
	 */
	var offset = caret.offset
	
	
	/**
	 * changes back the [CaretMovementUtil.offset] to the **last [CaretMovementUtil.commit]** and adds the given offset to it.
	 *
	 * @param offset the offset to be applied to the [CaretMovementUtil.offset] after resetting it
	 */
	fun reset(offset: Int) {
		if (caret.isValid) this.offset = caret.offset + offset
	}
	
	/**
	 * peeks and returns the character at the given offset from current [CaretMovementUtil.offset]
	 *
	 * @param offset the offset from current **temporary caret position**
	 * @return the character or null if the evaluated offset is invalid in the parent [Document] of the [Caret]
	 */
	fun peek(offset: Int): Char? {
		val documentChars = document.charsSequence
		val newOffset = this.offset + offset
		return if (newOffset >= 0 && newOffset < documentChars.length) documentChars[newOffset] else null
	}
	
	/**
	 * moves the temporary [CaretMovementUtil.offset]
	 *
	 * @param offset can be either positive or negative
	 */
	fun go(offset: Int) {
		this.offset += offset
	}
	
	/**
	 * moves the [caret] to the [CaretMovementUtil.offset]
	 *
	 * @param offset the offset to be added to [CaretMovementUtil.offset] before committing
	 */
	fun commit(offset: Int) {
		if (caret.isValid) caret.moveToOffset(offset.let { this.offset += it; this.offset })
	}
}
