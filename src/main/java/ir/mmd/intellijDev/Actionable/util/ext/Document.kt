package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.service

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
 * Just another overload for [Document.getText]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Document.getText(range: IntRange) = getText(TextRange(range.first, range.last))

/**
 * The [] syntax support for [Document]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun Document.get(index: Int) = immutableCharSequence[index]

/**
 * Returns the text of [line] in [Document]
 *
 * @see getLineText
 */
fun Document.getLineText(line: Int) = getText(getLineStartOffset(line)..getLineEndOffset(line))

/**
 * Returns the text of the line which [caret] is at, in the [Document]
 *
 * @see getLineText
 */
fun Document.getLineText(caret: Caret) = getLineText(getLineNumber(caret.offset))

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
 * Returns word boundaries located at [offset] in the [Document]
 *
 * @param separators characters that are known as **word delimiters**
 */
fun Document.getWordBoundaries(
	offset: Int,
	separators: String
): IntArray = intArrayOf(0, 0).apply {
	val chars = separators.toCharArray()
	
	if ((charAtOrNull(offset) ?: return@apply) in separators) {
		return@apply
	}
	
	set(
		0,
		immutableCharSequence.lastIndexOfAny(chars, offset - 1).let {
			if (it == -1) 0 else it + 1
		}
	)
	
	set(
		1,
		immutableCharSequence.indexOfAny(chars, offset + 1).let {
			if (it == -1) immutableCharSequence.lastIndex else it
		}
	)
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
	
	return if (startOffset == endOffset) null else {
		boundaries?.let {
			it[0] = startOffset
			it[1] = endOffset
		}
		
		immutableCharSequence.substring(startOffset, endOffset)
	}
}

/**
 * Same as [Document.getWordAtOffset] but catches the word backward if none found in front of the [offset].
 */
fun Document.getWordAtOffsetOrBefore(
	offset: Int,
	boundaries: IntArray?
): String? {
	return getWordAtOffset(offset, boundaries) ?: getWordAtOffset(offset - 1, boundaries)
}
