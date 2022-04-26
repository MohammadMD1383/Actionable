package ir.mmd.intellijDev.Actionable.caret.justification

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import ir.mmd.intellijDev.Actionable.util.runWriteCommandAction
import java.util.stream.Stream

/**
 * This class is used to manipulate editor carets
 */
class JustifyCaretUtil(private val editor: Editor) {
	private val project = editor.project!!
	private val document = editor.document
	private val carets = editor.caretModel.allCarets
	
	// /**
	//  * saves current {@link CaretState}s to restore later
	//  */
	// public void backupCarets() {
	// 	caretStates = editor.getCaretModel().getCaretsAndSelections();
	// }
	// /**
	//  * restores {@link CaretState}s previously backed up by {@link JustifyCaretUtil#backupCarets()}
	//  */
	// public void restoreCarets() {
	// 	assert caretStates != null;
	// 	editor.getCaretModel().setCaretsAndSelections(caretStates);
	// }
	
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
	private fun justify(targetColumn: Int) {
		project.runWriteCommandAction {
			for (caret in carets) {
				if (!caret.isValid) continue
				
				val currentLine = caret.logicalPosition.line
				val lineEndOffset = document.getLineEndOffset(currentLine)
				val lineLastColumn = editor.offsetToLogicalPosition(lineEndOffset).column
				if (lineLastColumn < targetColumn) document.insertString(lineEndOffset, " ".repeat(targetColumn - lineLastColumn))
				caret.moveToLogicalPosition(LogicalPosition(currentLine, targetColumn))
			}
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
		
		val targetColumn = rightmostColumn
		project.runWriteCommandAction {
			for (caret in carets) {
				val diff = targetColumn - caret.logicalPosition.column
				document.insertString(caret.offset - 1, " ".repeat(diff))
			}
		}
	}
	
	/**
	 * returns all [LogicalPosition]s of given carets as a [Stream]
	 *
	 * @return stream of [LogicalPosition]s from given carets
	 */
	private val allCaretPositionsStream: Stream<LogicalPosition>
		get() = carets.stream().map { obj: Caret -> obj.logicalPosition }
	
	/**
	 * returns the leftmost column position among given carets <br></br>
	 * please refer to [JustifyCaretUtil.justifyCaretsStart] for details
	 *
	 * @return the leftmost column position
	 */
	private val leftmostColumn: Int
		get() = allCaretPositionsStream.mapToInt { position: LogicalPosition -> position.column }.min().asInt
	
	/**
	 * same as [leftmostColumn] but returns the rightmost column position
	 *
	 * @return the rightmost column position
	 */
	private val rightmostColumn: Int
		get() = allCaretPositionsStream.mapToInt { position: LogicalPosition -> position.column }.max().asInt
	
	/**
	 * checks if there are more than on caret on a single line
	 *
	 * @return true if there are more than one caret on a single line, otherwise false
	 */
	private fun hasMoreThanOneCaretOnOneLine(): Boolean {
		return !allCaretPositionsStream.mapToInt { position: LogicalPosition -> position.line }.allMatch { e: Int -> HashSet<Any>().add(e) }
	}
}
