package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withMovementSettings

class ExecutePasteAction : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = withMovementSettings {
		val editor = e.editor
		val caret = e.primaryCaret
		val (target, action) = (editor.getUserData(scheduledPasteActionKind) ?: return).split(';')
		
		val startOffset: Int
		val endOffset: Int
		
		when (target) {
			"el" -> e.psiFile!!.elementAt(caret)!!.textRange.let { (start, end) ->
				startOffset = start
				endOffset = end
			}
			
			"wd" -> caret.util.getWordBoundaries(wordSeparators, hardStopCharacters).let { (start, end) ->
				startOffset = start
				endOffset = end
			}
			
			else -> throw Exception("Unknown scheduled paste action target")
		}
		
		e.project!!.runWriteCommandAction {
			paste(
				editor.document,
				caret,
				startOffset,
				endOffset,
				editor.getUserData(scheduledPasteActionOffset)!!,
				action == "ct"
			)
		}
		editor.removeScheduledPasteAction()
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
