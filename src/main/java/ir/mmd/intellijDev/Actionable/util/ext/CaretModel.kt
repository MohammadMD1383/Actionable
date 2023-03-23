package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.CaretModel
import ir.mmd.intellijDev.Actionable.action.LazyEventContext

/**
 * Adds a caret at the specified [offset] and returns the new created caret
 */
context (LazyEventContext)
@Suppress("NOTHING_TO_INLINE")
inline fun CaretModel.addCaret(offset: Int, makePrimary: Boolean = false) = addCaret(editor.offsetToLogicalPosition(offset), makePrimary)
