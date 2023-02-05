package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.markup.RangeHighlighter

/**
 * Returns an [IntRange] of [RangeHighlighter.getStartOffset] to [RangeHighlighter.getEndOffset]
 */
inline val RangeHighlighter.highlightRange: IntRange get() = startOffset..endOffset
