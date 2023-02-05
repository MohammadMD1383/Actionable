package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.find.FindResult

/**
 * Returns [FindResult.isStringFound]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun FindResult.component1(): Boolean = isStringFound

/**
 * Returns [FindResult.getStartOffset]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun FindResult.component2(): Int = startOffset

/**
 * Returns [FindResult.getEndOffset]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun FindResult.component3(): Int = endOffset
