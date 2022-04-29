package ir.mmd.intellijDev.Actionable.caret.justification

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import ir.mmd.intellijDev.Actionable.util.runWriteCommandAction

/**
 * This class is used to manipulate editor carets
 */
class JustifyCaretUtil(private val editor: Editor) {
	private val project = editor.project!!
	private val document = editor.document
	private val carets = editor.caretModel.allCarets
	
	/**
	 * moves all carets to leftmost active column between carets
	 *
	 * example:
	 *
	 * ```
	 * some text with car|et
	 * another wi|th caret
	 * the third one with a caret|
	 * ```
	 *
	 * will change to
	 *
	 * ```
	 * some text |with caret
	 * another wi|th caret
	 * the third |one with a caret
	 * ```
	 */
	fun justifyCaretsStart() = justify(leftmostColumn)
	
	/**
	 * same as [JustifyCaretUtil.justifyCaretsStart] but moves the carets to rightmost active column
	 */
	fun justifyCaretsEnd() = justify(rightmostColumn)
	
	/**
	 * aligns given carets across target column
	 *
	 * @param targetColumn the column that all carets will be aligned across to it
	 */
	private fun justify(targetColumn: Int) = project.runWriteCommandAction {
		for (caret in carets) {
			if (!caret.isValid) continue
			
			val currentLine = caret.logicalPosition.line
			val lineEndOffset = document.getLineEndOffset(currentLine)
			val lineLastColumn = editor.offsetToLogicalPosition(lineEndOffset).column
			if (lineLastColumn < targetColumn) document.insertString(lineEndOffset, " ".repeat(targetColumn - lineLastColumn))
			caret.moveToLogicalPosition(LogicalPosition(currentLine, targetColumn))
		}
	}
	
	/**
	 * moves all carets to rightmost active column between carets, and shifts the text
	 *
	 * example:
	 *
	 * ```
	 * int short |= 12;
	 * int mediumMedium |= 12;
	 * int largeLargeLarge |= 12;
	 * ```
	 *
	 * will change to <br></br>
	 *
	 * ```
	 * int short           |= 12;
	 * int mediumMedium    |= 12;
	 * int largeLargeLarge |= 12;
	 * ```
	 */
	fun justifyCaretsEndWithShifting() {
		if (hasMoreThanOneCaretOnOneLine()) return
		project.runWriteCommandAction {
			carets.forEach {
				document.insertString(it.offset - 1, " ".repeat(rightmostColumn - it.logicalPosition.column)) // todo: check if it works
			}
		}
	}
	
	/**
	 * returns the leftmost column position among given carets
	 *
	 * please refer to [JustifyCaretUtil.justifyCaretsStart] for details
	 *
	 * @return the leftmost column position
	 */
	private val leftmostColumn: Int get() = carets.minOf { it.logicalPosition.column }
	
	/**
	 * same as [leftmostColumn] but returns the rightmost column position
	 *
	 * @return the rightmost column position
	 */
	private val rightmostColumn: Int get() = carets.maxOf { it.logicalPosition.column }
	
	/**
	 * checks if there are more than on caret on a single line
	 *
	 * @return true if there are more than one caret on a single line, otherwise false
	 */
	private fun hasMoreThanOneCaretOnOneLine() = carets.distinctBy { it.logicalPosition.line }.size != carets.size
}
