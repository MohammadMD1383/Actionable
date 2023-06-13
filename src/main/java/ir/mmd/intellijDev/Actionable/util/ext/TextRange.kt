package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.util.TextRange

/**
 * Returns [TextRange.getStartOffset]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun TextRange.component1(): Int = startOffset

/**
 * Returns [TextRange.getEndOffset]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun TextRange.component2(): Int = endOffset

/**
 * Converts a [TextRange] into an [IntRange]
 */
inline val TextRange.intRange get() = startOffset..endOffset

@Suppress("NOTHING_TO_INLINE")
inline operator fun TextRange?.contains(i: Int) = this != null && contains(i)

/**
 * creates a new [TextRange] with [TextRange.getStartOffset] + [start] and [TextRange.getEndOffset] - [end]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun TextRange.innerSubRange(start: Int, end: Int) = TextRange(startOffset + start, endOffset - end)
