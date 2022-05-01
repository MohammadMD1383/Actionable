package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.markup.RangeHighlighter

inline val RangeHighlighter.highlightRange: IntRange get() = startOffset..endOffset
