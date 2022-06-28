package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.returnBy
import ir.mmd.intellijDev.Actionable.util.withService

inline fun Document.charAtOrNull(offset: Int) = charsSequence.getOrNull(offset)
inline fun Document.replaceCharAt(offset: Int, c: Char) = replaceString(offset, offset + 1, c.toString())
inline fun Document.replaceString(range: TextRange, s: CharSequence) = replaceString(range.startOffset, range.endOffset, s)
inline fun Document.getText(range: IntRange) = getText(TextRange(range.first, range.last))
inline operator fun Document.get(index: Int) = immutableCharSequence[index]

fun Document.getLineIndent(line: Int) = getText(getLineStartOffset(line)..getLineEndOffset(line)).takeWhile { it in " \t" }

fun Document.getWordBoundaries(
	offset: Int,
	separators: String
): IntArray = intArrayOf(0, 0).applyWith(separators.toCharArray(), charsSequence) { chars, sequence ->
	if ((charAtOrNull(offset) ?: return@applyWith) in separators) return@applyWith
	set(0, sequence.lastIndexOfAny(chars, offset - 1).let { if (it == -1) 0 else it + 1 })
	set(1, sequence.indexOfAny(chars, offset + 1).let { if (it == -1) sequence.lastIndex else it })
}

fun Document.getWordAtOffset(
	offset: Int,
	boundaries: IntArray? = null
): String? = withService<SettingsState, String?> {
	returnBy(getWordBoundaries(offset, wordSeparators + hardStopCharacters)) { (startOffset, endOffset) ->
		if (startOffset == endOffset) null else {
			boundaries?.let {
				it[0] = startOffset
				it[1] = endOffset
			}
			
			charsSequence.substring(startOffset, endOffset)
		}
	}
}
