package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class ExtractLineWithoutExtraWhitespaceAtCaret(private val cut: Boolean) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		
		e.project.runWriteCommandAction {
			editor.caretModel.allCarets.forEach {
				val lineNumber = document.getLineNumber(it.offset)
				val start = document.getLineStartOffset(lineNumber) + document.getLineStartIndentLength(lineNumber)
				val end = document.getLineEndOffset(lineNumber) - document.getLineTrailingWhitespaceLength(lineNumber)
				
				document.getText(TextRange(start, end)).copyToClipboard()
				if (cut) {
					document.deleteString(start, end)
				}
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun isDumbAware() = true
}

class CopyLineWithoutExtraWhitespaceAtCaretAction : ExtractLineWithoutExtraWhitespaceAtCaret(false)
class CutLineWithoutExtraWhitespaceAtCaretAction : ExtractLineWithoutExtraWhitespaceAtCaret(true)