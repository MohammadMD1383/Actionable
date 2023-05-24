package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.VisualPosition
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.CaretUtil

/**
 * Instantiates a [CaretUtil] with this [Caret]
 */
inline val Caret.util: CaretUtil get() = CaretUtil(this)

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

/**
 * Moves the caret forward by [count]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveForward(count: Int = 1) = moveToOffset(offset + count)

/**
 * Moves the caret backward by [count]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveBackward(count: Int = 1) = moveToOffset(offset - count)

/**
 * Moves the caret [count] lines up
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveUp(count: Int = 1) = moveToLogicalPosition(LogicalPosition(logicalPosition.line - count, logicalPosition.column))

/**
 * Moves the caret [count] lines down
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveDown(count: Int = 1) = moveToLogicalPosition(LogicalPosition(logicalPosition.line + count, logicalPosition.column))

/**
 * Replaces the text that is selected by the caret with [text]
 */
context (LazyEventContext)
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.replaceSelectedText(text: String, removeSelection: Boolean = false) {
	document.replaceString(selectionStart, selectionEnd, text)
	
	if (removeSelection) {
		removeSelection()
	} else {
		setSelection(selectionStart, selectionStart + text.length)
	}
}

/**
 * Replaces the selected text with the result of [text].
 *
 * The selected text will be passed to the lambda as an input.
 */
context (LazyEventContext)
inline fun Caret.replaceSelectedText(removeSelection: Boolean = false, text: (String) -> String) {
	replaceSelectedText(text(selectedText!!), removeSelection)
}

/**
 * Checks whether that all the carets have selection
 */
inline val List<Caret>.haveSelection get() = all { it.hasSelection() }

/**
 * Checks whether that all the carets don't have selection
 */
inline val List<Caret>.dontHaveSelection get() = !any { it.hasSelection() }
