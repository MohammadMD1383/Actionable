package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.CaretUtil

/**
 * Removes a character at [offset] in the [Document]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Document.removeCharAt(offset: Int) = deleteString(offset, offset + 1)

/**
 * Returns char at [offset] or `null` if [offset] is out of bounds
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Document.charAtOrNull(offset: Int) = charsSequence.getOrNull(offset)

/**
 * The [] syntax support for [Document]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun Document.get(index: Int) = immutableCharSequence[index]

/**
 * Returns the text of [line] in [Document]
 *
 * @param boundaries (optional) in order to receive start and end offset of the line.
 * @see getLineText
 */
fun Document.getLineText(line: Int, boundaries: IntArray? = null): String {
	val lineStartOffset = getLineStartOffset(line)
	val lineEndOffset = getLineEndOffset(line)
	
	boundaries?.let {
		it[0] = lineStartOffset
		it[1] = lineEndOffset
	}
	
	return getText(TextRange(lineStartOffset, lineEndOffset))
}

/**
 * Returns the text of the line which [caret] is at, in the [Document]
 *
 * @param boundaries (optional) in order to receive the start and end offset of the line.
 * @see getLineText
 */
fun Document.getLineText(caret: Caret, boundaries: IntArray? = null) = getLineText(getLineNumber(caret.offset), boundaries)

/**
 * Returns starting indentation of the [line] in the [Document]
 */
fun Document.getLineStartIndent(line: Int) = getLineText(line).takeWhile { it in " \t" }

/**
 * Returns trailing whitespace of the [line] in the [Document]
 */
fun Document.getLineTrailingWhitespace(line: Int) = getLineText(line).takeLastWhile { it in " \t" }

/**
 * Returns starting indentation characters count (count of whitespaces) of the [line] in the [Document]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Document.getLineStartIndentLength(line: Int) = getLineStartIndent(line).length

/**
 * Returns trailing whitespace characters of the [line] in the [Document]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Document.getLineTrailingWhitespaceLength(line: Int) = getLineTrailingWhitespace(line).length

/**
 * Returns the last index in the document char[]
 */
inline val Document.lastIndex get() = textLength - 1

/**
 * Returns word boundaries located at [offset] in the [Document]
 *
 * @param separators characters that are known as **word delimiters**
 */
fun Document.getWordBoundaries(
	offset: Int,
	separators: String
): IntArray {
	val boundaries = intArrayOf(offset, offset)
	
	while (true) {
		val c = charAtOrNull(boundaries[1])
		
		if (c == null || c in separators) {
			break
		}
		
		boundaries[1]++
	}
	
	while (true) {
		val c = charAtOrNull(boundaries[0] - 1)
		
		if (c == null || c in separators) {
			break
		}
		
		boundaries[0]--
	}
	
	return boundaries
}

/**
 * Calls [getWordBoundaries] overload with the default `separators + hardStops` from settings
 */
fun Document.getWordBoundaries(offset: Int): IntArray {
	return getWordBoundaries(offset, service<SettingsState>().run { wordSeparators + hardStopCharacters })
}

/**
 * Returns word at [offset] in the [Document]
 *
 * @param boundaries (Optional) you can set this to get word boundaries (start and end offset in the [Document]) as well as the word itself.
 * @see [Document.getWordBoundaries]
 */
fun Document.getWordAtOffset(
	offset: Int,
	boundaries: IntArray? = null
): String? {
	val (startOffset, endOffset) = getWordBoundaries(offset)
	boundaries?.let {
		it[0] = startOffset
		it[1] = endOffset
	}
	
	return if (startOffset == endOffset) null else immutableCharSequence.substring(startOffset, endOffset)
}

/**
 * Matches a number literal under caret
 *
 * @param boundaries specify to get the boundaries of the number in the [Document]
 * @return null if not matched
 */
fun Document.getNumberAt(caret: Caret, boundaries: IntArray? = null): String? {
	val cutil = caret.util
	val numbers = "0123456789"
	
	cutil.moveWhileFacing(numbers, "", CaretUtil.BACKWARD)
	val start = cutil.offset
	
	cutil.reset()
	cutil.moveWhileFacing(numbers, "", CaretUtil.FORWARD)
	val end = cutil.offset
	
	boundaries?.let { it[0] = start; it[1] = end }
	return if (start == end) null else getText(TextRange(start, end))
}
