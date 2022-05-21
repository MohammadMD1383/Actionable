package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.LogicalPosition
import ir.mmd.intellijDev.Actionable.util.CaretUtil

inline val Caret.selectionRange: IntRange get() = selectionStart..selectionEnd
inline val Caret.util: CaretUtil get() = CaretUtil(this)

inline fun Caret.setSelection(range: IntRange) = setSelection(range.first, range.last)
inline fun Caret.moveBackward() = moveToOffset(offset - 1)
inline fun Caret.moveForward() = moveToOffset(offset + 1)
inline fun Caret.moveUp() = moveToLogicalPosition(LogicalPosition(logicalPosition.line - 1, logicalPosition.column))
inline fun Caret.moveDown() = moveToLogicalPosition(LogicalPosition(logicalPosition.line + 1, logicalPosition.column))

inline infix fun Caret.moveTo(offset: Int) = moveToOffset(offset)
inline infix fun Caret.moveTo(position: LogicalPosition) = moveToLogicalPosition(position)
