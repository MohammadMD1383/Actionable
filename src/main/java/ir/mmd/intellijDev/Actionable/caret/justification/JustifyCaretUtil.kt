package ir.mmd.intellijDev.Actionable.caret.justification

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import ir.mmd.intellijDev.Actionable.util.ext.doIf
import ir.mmd.intellijDev.Actionable.util.ext.forEachIf
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction

class JustifyCaretUtil(private val editor: Editor) {
	private val project = editor.project!!
	private val document = editor.document
	private val carets = editor.caretModel.allCarets
	
	private val leftmostColumn: Int get() = carets.minOf { it.visualPosition.column }
	private val rightmostColumn: Int get() = carets.maxOf { it.logicalPosition.column }
	private val hasJustOneCaretOnEachLine get() = carets.distinctBy { it.logicalPosition.line }.size == carets.size
	
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
		carets.forEachIf({ isValid }) {
			val currentLine = it.logicalPosition.line
			val lineEndOffset = document.getLineEndOffset(currentLine)
			val lineLastColumn = editor.offsetToLogicalPosition(lineEndOffset).column
			if (lineLastColumn < targetColumn) document.insertString(lineEndOffset, " ".repeat(targetColumn - lineLastColumn))
			it.moveToLogicalPosition(LogicalPosition(currentLine, targetColumn))
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
	fun justifyCaretsEndWithShifting() = doIf({ hasJustOneCaretOnEachLine }) {
		project.runWriteCommandAction {
			carets.forEach {
				document.insertString(
					it.offset + if (it.logicalPosition.leansForward) -1 else 0,
					" ".repeat(rightmostColumn - it.logicalPosition.column)
				)
			}
		}
	}
}
