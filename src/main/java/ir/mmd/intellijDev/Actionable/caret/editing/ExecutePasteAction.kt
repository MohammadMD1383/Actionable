package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.getWordAtCaret
import ir.mmd.intellijDev.Actionable.util.*

class ExecutePasteAction : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor!!
		val document = editor.document
		val caret = e.primaryCaret!!
		val pasteOffset = editor.getUserData(scheduledPasteActionOffset)!!
		val (target, action) = (editor.getUserData(scheduledPasteActionKind) ?: return).split(';')
		
		val startOffset: Int
		val endOffset: Int
		
		if (target == "el") e.psiFile!!.elementAt(caret)!!.textRange.let {
			startOffset = it.startOffset
			endOffset = it.endOffset
		} else  /* target == "wd" */ {
			val wordBoundaries = intArrayOf(0, 0)
			getWordAtCaret(document, caret, wordBoundaries)
			startOffset = wordBoundaries[0]
			endOffset = wordBoundaries[1]
		}
		
		e.project!!.runWriteCommandAction { paste(document, caret, startOffset, endOffset, pasteOffset, action == "ct") }
		removeScheduledPasteAction(editor)
	}
	
	/**
	 * will paste the specified range of text into the specified offset
	 * and will take care of *cut* action
	 *
	 * @param document instance of the [Document]
	 * @param caret    the caret to move to the final offset after the paste operation
	 * @param start    text start position
	 * @param end      text end position
	 * @param offset   offset to paste at
	 * @param isCutAction    whether to delete the text after pasting or not
	 */
	private fun paste(
		document: Document,
		caret: Caret,
		start: Int,
		end: Int,
		offset: Int,
		isCutAction: Boolean
	) {
		if (start == end) return // todo check
		val text = document.getText(TextRange(start, end))
		
		if (offset > start && offset > end) {
			document.insertString(offset, text)
			caret.moveToOffset(offset + text.length)
			if (isCutAction) document.deleteString(start, end)
		} else if (offset < start && offset < end) {
			if (isCutAction) document.deleteString(start, end)
			document.insertString(offset, text)
			caret.moveToOffset(offset + text.length)
		} /* else -> offset is between the start and end -> ignore */
	}
}
