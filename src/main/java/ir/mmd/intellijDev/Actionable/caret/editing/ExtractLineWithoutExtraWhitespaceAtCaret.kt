package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretActionWithInitialization
import ir.mmd.intellijDev.Actionable.util.ext.copyToClipboard
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.getLineStartIndentLength
import ir.mmd.intellijDev.Actionable.util.ext.getLineTrailingWhitespaceLength

abstract class ExtractLineWithoutExtraWhitespaceAtCaret(private val cut: Boolean) : MultiCaretActionWithInitialization<MutableList<String>>() {
	context(LazyEventContext)
	override fun initialize(): MutableList<String> = mutableListOf()
	
	context(LazyEventContext)
	override fun finalize() {
		data.joinToString(separator = "\n").copyToClipboard()
	}
	
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val lineNumber = document.getLineNumber(caret.offset)
		val start = document.getLineStartOffset(lineNumber) + document.getLineStartIndentLength(lineNumber)
		val end = document.getLineEndOffset(lineNumber) - document.getLineTrailingWhitespaceLength(lineNumber)
		
		data += document.getText(TextRange(start, end))
		if (cut) {
			runWriteCommandAction {
				document.deleteString(start, end)
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun isDumbAware() = true
}

class CopyLineWithoutExtraWhitespaceAtCaretAction : ExtractLineWithoutExtraWhitespaceAtCaret(false)
class CutLineWithoutExtraWhitespaceAtCaretAction : ExtractLineWithoutExtraWhitespaceAtCaret(true)
