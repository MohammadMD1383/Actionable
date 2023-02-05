package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.VisualPosition

/**
 * Returns [LogicalPosition.line]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun LogicalPosition.component1() = line

/**
 * Returns [LogicalPosition.column]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun LogicalPosition.component2() = column

/**
 * Returns [VisualPosition.line]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun VisualPosition.component1() = line

/**
 * Returns [VisualPosition.column]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun VisualPosition.component2() = column
