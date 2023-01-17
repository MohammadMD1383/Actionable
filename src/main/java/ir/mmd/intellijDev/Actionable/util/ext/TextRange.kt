package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.util.TextRange

inline operator fun TextRange.component1(): Int = startOffset
inline operator fun TextRange.component2(): Int = endOffset

@Suppress("ReplaceRangeToWithUntil")
inline val TextRange.exclusiveBothAsIntRange get() = startOffset + 1..endOffset - 1

inline val TextRange.intRange get() = startOffset..endOffset

inline operator fun TextRange?.contains(i: Int) = this != null && i in this
