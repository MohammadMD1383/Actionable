package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.VisualPosition
import ir.mmd.intellijDev.Actionable.util.CaretUtil

/**
 * Same as [Caret.getSelectionRange] but this one returns an [IntRange]
 *
 * This is for compatibility with older IDE versions
 */
inline val Caret.selectionRangeCompat: IntRange get() = selectionStart..selectionEnd

/**
 * Instantiates a [CaretUtil] with this [Caret]
 */
inline val Caret.util: CaretUtil get() = CaretUtil(this)

/**
 * Moves the [Caret] one character back
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveBackward() = moveToOffset(offset - 1)

/**
 * Moves the [Caret] one character forward
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveForward() = moveToOffset(offset + 1)

/**
 * Moves the [Caret] one line up
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveUp() = moveToLogicalPosition(LogicalPosition(logicalPosition.line - 1, logicalPosition.column))

/**
 * Moves the [Caret] one line down
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveDown() = moveToLogicalPosition(LogicalPosition(logicalPosition.line + 1, logicalPosition.column))

/**
 * Simple sugar replacement for [Caret.moveToOffset]
 */
@Suppress("NOTHING_TO_INLINE")
inline infix fun Caret.moveTo(offset: Int) = moveToOffset(offset)

/**
 * Simple sugar replacement for [Caret.moveToLogicalPosition]
 */
@Suppress("NOTHING_TO_INLINE")
inline infix fun Caret.moveTo(position: LogicalPosition) = moveToLogicalPosition(position)

/**
 * Simple sugar replacement for [Caret.moveToVisualPosition]
 */
@Suppress("NOTHING_TO_INLINE")
inline infix fun Caret.moveTo(position: VisualPosition) = moveToVisualPosition(position)
