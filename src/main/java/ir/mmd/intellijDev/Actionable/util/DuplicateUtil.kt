package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.VisualPosition
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction

/**
 * Duplicates line(s) up/down
 */
class DuplicateUtil(private val editor: Editor) {
	/**
	 * [com.intellij.openapi.project.Project] associated with the [editor]
	 */
	private val project = editor.project!!
	
	/**
	 * [com.intellij.openapi.editor.Document] associated with the [editor]
	 */
	private val document = editor.document
	
	/**
	 * duplicate line(s) up
	 *
	 *  * if `start offset` and `end offset` are same, then the line will be duplicated <br></br>
	 *  * if not, then the **lines including start offset and end offset** will be duplicated
	 *
	 * *start offset and end offset are caret offsets in the active editor document*
	 *
	 * @param start start offset of duplication range
	 * @param end   end offset of duplication range
	 */
	fun duplicateUp(
		start: Int,
		end: Int
	) = with(getDuplicateString(start, end)) {
		project.runWriteCommandAction {
			document.insertString(endOffset, "\n${text}")
		}
	}
	
	/**
	 * please refer to [DuplicateUtil.duplicateUp] for documentation
	 */
	fun duplicateDown(
		start: Int,
		end: Int
	) = with(getDuplicateString(start, end)) {
		project.runWriteCommandAction {
			document.insertString(startOffset, "${text}\n")
			
			// check if the caret is at line start,
			// then move the caret manually.
			// due to an issue that caret won't move automatically.
			// to be more clear, you can comment out the statements below and see the effect.
			// put the caret at the line start and fire the `duplicate down` action.
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
	 * retrieves the line(s) of the document to be duplicated <br></br>
	 * more info at [DuplicateUtil.duplicateUp]
	 *
	 * @param start starting caret offset of duplication range
	 * @param end   ending caret offset of duplication range
	 * @return selected line(s) for duplication
	 */
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
			endingLine = document.getLineNumber(end)
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
	 * this class contains information about the text that is going to be duplicated
	 */
	private data class DuplicateString(
		val startingLine: Int,
		val endingLine: Int,
		val startOffset: Int,
		val endOffset: Int,
		val text: String
	)
}
