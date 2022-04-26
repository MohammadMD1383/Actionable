package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.getElementAtCaret
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.getWordAtCaret
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.removeScheduledPasteAction
import ir.mmd.intellijDev.Actionable.caret.editing.Actions.setActionAvailability
import ir.mmd.intellijDev.Actionable.util.runWriteCommandAction

class ExecutePasteAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project!!
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		val kind = editor.getUserData(Actions.scheduledPasteActionKind)
		val pasteOffset = editor.getUserData(Actions.scheduledPasteActionOffset)
		if (kind != null) {
			val commands = kind.split(';')
			val isCut = commands[1] == "ct"
			if (commands[0] == "el") {
				val element = getElementAtCaret(project, document, caret)
				val elementRange = element!!.textRange
				WriteCommandAction.runWriteCommandAction(project) {
					paste(
						document,
						caret,
						elementRange.startOffset,
						elementRange.endOffset,
						pasteOffset!!,
						isCut
					)
				}
			} else  /* commands[0] == "wd" */ {
				val wb = IntArray(2)
				val word = getWordAtCaret(document, caret, wb)
				if (word != null) {
					project.runWriteCommandAction {
						paste(
							document,
							caret,
							wb[0],
							wb[1],
							pasteOffset!!,
							isCut
						)
					}
				}
			}
			removeScheduledPasteAction(editor)
		}
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
	 * @param isCut    whether to delete the text after pasting or not
	 */
	private fun paste(
		document: Document,
		caret: Caret,
		start: Int,
		end: Int,
		offset: Int,
		isCut: Boolean
	) {
		val text = document.getText(TextRange(start, end))
		
		/*
		  this is actually for elements, because we have just elements that contain whitespaces.
		  words won't contain whitespaces.
		*/
		if (text.isBlank()) return
		
		if (offset > start && offset > end) {
			document.insertString(offset, text)
			caret.moveToOffset(offset + text.length)
			if (isCut) document.deleteString(start, end)
		} else if (offset < start && offset < end) {
			if (isCut) document.deleteString(start, end)
			document.insertString(offset, text)
			caret.moveToOffset(offset + text.length)
		} /* else -> offset is between the start and end: ignore */
	}
	
	override fun update(e: AnActionEvent) = setActionAvailability(e)
}
