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
