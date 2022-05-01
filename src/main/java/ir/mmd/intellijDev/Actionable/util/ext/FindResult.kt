package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.find.FindResult

inline operator fun FindResult.component1(): Boolean = isStringFound
inline operator fun FindResult.component2(): Int = startOffset
inline operator fun FindResult.component3(): Int = endOffset
