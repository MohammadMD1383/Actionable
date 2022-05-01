package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.util.CaretUtil
import ir.mmd.intellijDev.Actionable.util.countRepeats
import ir.mmd.intellijDev.Actionable.util.returnBy
import ir.mmd.intellijDev.Actionable.util.withMovementSettings

inline fun Document.charAtOrNull(offset: Int) = charsSequence.getOrNull(offset)

fun Document.findOffsetOfNext(
	startOffset: Int,
	chars: String,
	dir: Int
) = countRepeats(startOffset, dir) { (charAtOrNull(it) ?: return@countRepeats false) !in chars } + dir

fun Document.getWordBoundaries(
	offset: Int,
	separators: String,
	hardStops: String
): IntArray = returnBy(intArrayOf(0, 0)) {
	return if ((charAtOrNull(offset) ?: return it) in separators + hardStops) it
	else intArrayOf(
		findOffsetOfNext(offset - 1, separators + hardStops, CaretUtil.BACKWARD) + 1,
		findOffsetOfNext(offset + 1, separators + hardStops, CaretUtil.FORWARD) - 1
	)
}

fun Document.getWordAtOffset(
	offset: Int,
	boundaries: IntArray? = null
): String? = withMovementSettings {
	returnBy(getWordBoundaries(offset, wordSeparators, hardStopCharacters)) { (startOffset, endOffset) ->
		return@returnBy if (startOffset == endOffset) null else {
			boundaries?.let {
				it[0] = startOffset
				it[1] = endOffset
			}
			
			charsSequence.substring(startOffset, endOffset)
		}
	}
}
