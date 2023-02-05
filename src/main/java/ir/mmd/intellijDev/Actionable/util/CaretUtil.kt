package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.charAtOrNull
import ir.mmd.intellijDev.Actionable.util.ext.isPositive
import kotlin.math.max
import kotlin.math.min

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
	
	/**
	 * [Document] associated with [caret]
	 */
	val document = caret.editor.document
	
	/**
	 * temporary [caret] offset
	 */
	var offset = caret.offset
	
	/**
	 * Peeks next character (in front of [caret]) or `null` if there is no character in front of the [caret]
	 */
	inline val nextChar get() = peek(+1)
	
	/**
	 * Peeks previous character (in back of [caret]) or `null` if there is no character in back of the [caret]
	 */
	inline val prevChar get() = peek(-1)
	
	/**
	 * Returns the offset of the next character (in front of the [caret]) in the [document]
	 */
	inline val nextCharOffset get() = offset
	
	/**
	 * Returns the offset of the previous character (in back of the [caret]) in the [document]
	 */
	inline val prevCharOffset get() = offset - 1
	
	/**
	 * reset [offset] to [Caret.getOffset]
	 */
	fun reset() {
		offset = caret.offset
	}
	
	/**
	 * Move the [caret] to the [offset]
	 */
	fun commit() = caret.moveToOffset(offset)
	
	/**
	 * Makes a caret selection from [Caret.getOffset] to [CaretUtil.offset]
	 *
	 * **This method will take care of selection offsets to be from lower to higher**
	 */
	fun makeOffsetDiffSelection() {
		val caretOffset = caret.offset
		caret.setSelection(min(caretOffset, offset), max(caretOffset, offset))
	}
	
	/**
	 * Translates a character relative position to a relative caret offset
	 *
	 * Example: *(| is caret)*
	 * ```kotlin
	 * class Hello|World { ... }
	 * ```
	 * the relative `W` position (in `World`) to caret is `+1` but it will be translated to `0`
	 * as a relative offset
	 *
	 * @see peek
	 */
	private fun relativePositionToOffset(pos: Int): Int = offset + if (pos.isPositive) pos - 1 else pos
	
	/**
	 * peeks and returns the character at the given offset from current [CaretUtil.offset]
	 *
	 * @param dir the offset from current **temporary caret position**
	 * @return the character or null if the evaluated offset is invalid in the parent [Document] of the [Caret]
	 */
	fun peek(dir: Int): Char? = if (dir == 0) null else document.charAtOrNull(relativePositionToOffset(dir))
	
	private fun move(step: Int, hardStops: String): Boolean {
		(peek(step) ?: return false).let {
			offset += step
			return it !in hardStops
		}
	}
	
	/**
	 * Moves the caret **(virtually)** while facing [chars] but immediately stop if reached any of [hardStops]
	 *
	 * @return `false` if we reached a [hardStops] otherwise `true`
	 * @see offset
	 */
	private fun moveWhileFacing(chars: String, hardStops: String, step: Int): Boolean {
		while (true) when (peek(step) ?: return false) {
			in hardStops -> return false
			!in chars -> return true
			else -> offset += step
		}
	}
	
	/**
	 * Moves the caret **(virtually)** until reaching any of the [chars] but immediately stop if reached any of [hardStops]
	 *
	 * @return `false` if we reached a [hardStops] otherwise `true`
	 * @see offset
	 */
	fun moveUntilReached(chars: String, hardStops: String, step: Int): Boolean {
		while (true) when (peek(step) ?: return false) {
			in hardStops -> return false
			in chars -> return true
			else -> offset += step
		}
	}
	
	/**
	 * Moves the caret in a complex scenario
	 *
	 * @param separators word separators
	 * @param hardStops immediately stop at these characters no matter where
	 * @param mode see [SettingsState.WSBehaviour]
	 * @param dir [FORWARD] or [BACKWARD]
	 * @param commit (Optional) whether to [commit] the new [offset] or not after moving
	 */
	fun moveCaret(
		separators: String,
		hardStops: String,
		mode: Int,
		dir: Int,
		commit: Boolean = true
	) {
		val char = peek(dir) ?: return
		
		if (!move(dir, hardStops)) return afterDoing {
			if (commit) {
				commit()
			}
		}
		
		when (mode) {
			SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE -> {
				if (char in separators) {
					moveWhileFacing(separators, hardStops, dir)
				} else {
					moveUntilReached(separators, hardStops, dir)
				}
			}
			
			SettingsState.WSBehaviour.STOP_AT_NEXT_SAME_CHAR_TYPE -> {
				if (char in separators) {
					moveWhileFacing(separators, hardStops, dir) and moveUntilReached(separators, hardStops, dir)
				} else {
					moveUntilReached(separators, hardStops, dir) and moveWhileFacing(separators, hardStops, dir)
				}
			}
		}
		
		if (commit) {
			commit()
		}
	}
	
	/**
	 * Returns boundaries of the work which is under the caret
	 *
	 * @see moveCaret
	 */
	fun getWordBoundaries(
		wordSeparators: String,
		hardStops: String
	) = IntArray(2).apply {
		moveUntilReached(wordSeparators, hardStops, BACKWARD)
		set(0, offset)
		reset()
		
		moveUntilReached(wordSeparators, hardStops, FORWARD)
		set(1, offset)
		reset()
	}
	
	/**
	 * Returns the word under the caret or `null` if there is no word
	 *
	 * @param boundaries specify an `int[2]` if you want to get the word boundaries too
	 * @see getWordBoundaries
	 */
	fun getAssociatedWord(boundaries: IntArray? = null): String? = withService<SettingsState, String?> {
		val (startOffset, endOffset) = getWordBoundaries(wordSeparators, hardStopCharacters)
		
		return if (startOffset == endOffset) null else {
			boundaries?.let {
				it[0] = startOffset
				it[1] = endOffset
			}
			
			document.immutableCharSequence.substring(startOffset, endOffset)
		}
	}
}
