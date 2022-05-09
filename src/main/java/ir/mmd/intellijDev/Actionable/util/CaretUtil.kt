package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.charAtOrNull
import ir.mmd.intellijDev.Actionable.util.ext.contains
import ir.mmd.intellijDev.Actionable.util.ext.isPositive

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
	
	private fun reset() {
		offset = caret.offset
	}
	
	private fun commit() = caret.moveToOffset(offset)
	
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
	
	private fun moveWhileFacing(chars: String, hardStops: String, step: Int): Boolean {
		while (true) when (peek(step) ?: return false) {
			in hardStops -> return false
			!in chars -> return true
			else -> offset += step
		}
	}
	
	fun moveUntilReached(chars: String, hardStops: String, step: Int): Boolean {
		while (true) when (peek(step) ?: return false) {
			in hardStops -> return false
			in chars -> return true
			else -> offset += step
		}
	}
	
	fun moveCaret(
		separators: String,
		hardStops: String,
		mode: Int,
		dir: Int,
		commit: Boolean = true
	) = nonnull(peek(dir)) {
		if (!move(dir, hardStops)) return@nonnull after { if (commit) commit() }
		when (mode) {
			SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE -> if (it in separators)
				moveWhileFacing(separators, hardStops, dir)
			else
				moveUntilReached(separators, hardStops, dir)
			
			SettingsState.WSBehaviour.STOP_AT_NEXT_SAME_CHAR_TYPE -> if (it in separators)
				moveWhileFacing(separators, hardStops, dir) and moveUntilReached(separators, hardStops, dir)
			else
				moveUntilReached(separators, hardStops, dir) and moveWhileFacing(separators, hardStops, dir)
		}
		if (commit) commit()
	}
	
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
	
	fun getAssociatedWord(boundaries: IntArray? = null): String? = withService<SettingsState, String?> {
		returnBy(getWordBoundaries(wordSeparators, hardStopCharacters)) { (startOffset, endOffset) ->
			return@returnBy if (startOffset == endOffset) null else {
				boundaries?.let {
					it[0] = startOffset
					it[1] = endOffset
				}
				
				document.charsSequence.substring(startOffset, endOffset)
			}
		}
	}
}
