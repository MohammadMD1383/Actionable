package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.util.TextRange

inline operator fun TextRange.component1(): Int = startOffset
inline operator fun TextRange.component2(): Int = endOffset
