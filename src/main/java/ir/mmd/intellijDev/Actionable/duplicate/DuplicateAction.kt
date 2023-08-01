package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.VisualPosition
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction

abstract class DuplicateAction : MultiCaretAction(), DumbAware {
	/**
	 * Duplicate line(s) up
	 *
	 *  * if `start offset` and `end offset` are same, then the line will be duplicated <br></br>
	 *  * if not, then the **lines including start offset and end offset** will be duplicated
	 *
	 * *Start offset and end offset are caret offsets in the active editor document*
	 *
	 * @param start start offset of duplication range
	 * @param end   end offset of duplication range
	 */
	context (LazyEventContext)
	fun duplicateUp(
		start: Int,
		end: Int
	) = with(getDuplicateString(start, end)) {
		runWriteCommandAction {
			document.insertString(endOffset, "\n${text}")
		}
	}
	
	/**
	 * Please refer to [duplicateUp] for documentation.
	 */
	context (LazyEventContext)
	fun duplicateDown(
		start: Int,
		end: Int
	) = with(getDuplicateString(start, end)) {
		runWriteCommandAction {
			document.insertString(startOffset, "${text}\n")
			
			// Check if the caret is at line start,
			// then move the caret manually.
			// Due to an issue that caret won't move automatically.
			// To be more clear, you can comment out the statements below and see the effect.
			// Put the caret at the line start and fire the `duplicate down` action.
			if (startOffset == start) (editor.caretModel.getCaretAt(VisualPosition(startingLine, 0)) ?: return@runWriteCommandAction)
				.moveToLogicalPosition(
					LogicalPosition(
						startingLine + (endingLine - startingLine + 1),
						0
					)
				)
		}
	}
	
	/**
	 * Retrieves the line(s) of the document to be duplicated.<br></br>
	 * more info at [duplicateUp]
	 *
	 * @param start starting caret offset of duplication range
	 * @param end   ending caret offset of duplication range
	 * @return selected line(s) for duplication
	 */
	context (LazyEventContext)
	private fun getDuplicateString(
		start: Int,
		end: Int
	): DuplicateString {
		val startingLine: Int
		val endingLine: Int
		val startOffset: Int
		val endOffset: Int
		
		if (start != end) /* has selection */ {
			startingLine = document.getLineNumber(start)
			endingLine = document.getLineNumber(end - 1)
			startOffset = document.getLineStartOffset(startingLine)
			endOffset = document.getLineEndOffset(endingLine)
		} else /* no selection */ {
			endingLine = document.getLineNumber(start)
			startingLine = endingLine
			startOffset = document.getLineStartOffset(startingLine)
			endOffset = document.getLineEndOffset(startingLine)
		}
		
		return DuplicateString(
			startingLine,
			endingLine,
			startOffset,
			endOffset,
			document.getText(TextRange(startOffset, endOffset))
		)
	}
	
	/**
	 * This class contains information about the text that is going to be duplicated.
	 */
	private data class DuplicateString(
		val startingLine: Int,
		val endingLine: Int,
		val startOffset: Int,
		val endOffset: Int,
		val text: String
	)
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class DuplicateLinesUp : DuplicateAction() {
	context(LazyEventContext)
	override fun perform() = duplicateUp(caret.selectionStart, caret.selectionEnd)
}

class DuplicateLinesDown : DuplicateAction() {
	context(LazyEventContext)
	override fun perform() = duplicateDown(caret.selectionStart, caret.selectionEnd)
}
