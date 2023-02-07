package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.SingleCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class DuplicateLineAndPasteClipboardContentAction : SingleCaretAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) {
		val hasSelection = caret.hasSelection()
		
		if (hasSelection && '\n' in caret.selectedText!!) {
			return
		}
		
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String)
			.split("\n") as MutableList
		
		val selectionStart = caret.selectionStart
		val selectionEnd = caret.selectionEnd
		val selectionLength = selectionEnd - selectionStart
		val lineNumber = document.getLineNumber(selectionStart)
		val lineStart = document.getLineStartOffset(lineNumber)
		var lineEnd = document.getLineEndOffset(lineNumber)
		val text = document.getText(TextRange(lineStart, lineEnd))
		val insertionStartOffset = selectionStart - lineStart
		val insertionEndOffset = selectionEnd - lineStart
		
		project.runWriteCommandAction {
			val first = contents.removeFirst()
			val firstLength = first.length
			
			document.replaceString(selectionStart, selectionEnd, first)
			caret moveTo selectionStart + firstLength
			if (hasSelection) {
				caret.setSelection(selectionStart, selectionStart + firstLength)
			}
			lineEnd += firstLength - selectionLength
			
			contents.forEach {
				document.insertString(lineEnd, "\n${text.replaceRange(insertionStartOffset, insertionEndOffset, it)}")
				
				val contentLength = it.length
				val replacementStart = lineEnd + 1 + insertionStartOffset // +1 due to \n
				val newCaret = caretModel.addCaret(
					editor.offsetToLogicalPosition(replacementStart + contentLength),
					false
				)
				
				if (hasSelection) {
					newCaret?.setSelection(replacementStart, replacementStart + contentLength)
				}
				lineEnd += 1 + text.length + (contentLength - selectionLength) // +1 due to \n
			}
		}
	}
	
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun isDumbAware() = true
}
