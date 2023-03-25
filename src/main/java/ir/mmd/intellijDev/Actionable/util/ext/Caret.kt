package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.VisualPosition
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
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
 * Moves the [Caret] [n] characters back
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveBackward(n: Int = 1) = moveToOffset(offset - n)

/**
 * Moves the [Caret] [n] characters forward
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveForward(n: Int = 1) = moveToOffset(offset + n)

/**
 * Moves the [Caret] [n] lines up
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveUp(n: Int = 1) = moveToLogicalPosition(LogicalPosition(logicalPosition.line - n, logicalPosition.column))

/**
 * Moves the [Caret] [n] lines down
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Caret.moveDown(n: Int = 1) = moveToLogicalPosition(LogicalPosition(logicalPosition.line + n, logicalPosition.column))

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
