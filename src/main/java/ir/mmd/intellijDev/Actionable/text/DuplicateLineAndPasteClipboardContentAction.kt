package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.SingleCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.moveForward
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class DuplicateLineAndPasteClipboardContentAction : SingleCaretAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) {
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String)
			.split("\n") as MutableList
		
		val caretOffset = caret.offset
		val lineNumber = document.getLineNumber(caretOffset)
		val start = document.getLineStartOffset(lineNumber)
		var end = document.getLineEndOffset(lineNumber)
		val text = document.getText(TextRange(start, end))
		val insertionOffset = caretOffset - start
		
		project.runWriteCommandAction {
			val first = contents.removeFirst()
			document.insertString(caretOffset, first)
			caret.moveForward(first.length)
			end += first.length
			
			contents.forEach {
				document.insertString(end, "\n${text.replaceRange(insertionOffset, insertionOffset, it)}")
				caretModel.addCaret(editor.offsetToLogicalPosition(end + insertionOffset + it.length + 1), false)
				end += text.length + it.length + 1
			}
		}
	}
	
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun isDumbAware() = true
}
