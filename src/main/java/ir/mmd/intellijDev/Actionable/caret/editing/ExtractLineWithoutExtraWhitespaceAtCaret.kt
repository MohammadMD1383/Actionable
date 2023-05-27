package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.copyToClipboard
import ir.mmd.intellijDev.Actionable.util.ext.getLineStartIndentLength
import ir.mmd.intellijDev.Actionable.util.ext.getLineTrailingWhitespaceLength

abstract class ExtractLineWithoutExtraWhitespaceAtCaret(private val cut: Boolean) : MultiCaretAction(), DumbAware {
	private var lines: MutableList<String>? = null
	
	context(LazyEventContext)
	override fun initialize() {
		lines = mutableListOf()
	}
	
	context(LazyEventContext)
	override fun finalize() {
		lines!!.joinToString(separator = "\n").copyToClipboard()
		lines = null
	}
	
	context (LazyEventContext)
	override fun perform() {
		val lineNumber = document.getLineNumber(caret.offset)
		val start = document.getLineStartOffset(lineNumber) + document.getLineStartIndentLength(lineNumber)
		val end = document.getLineEndOffset(lineNumber) - document.getLineTrailingWhitespaceLength(lineNumber)
		
		lines!! += document.getText(TextRange(start, end))
		if (cut) {
			runWriteCommandAction {
				document.deleteString(start, end)
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class CopyLineWithoutExtraWhitespaceAtCaretAction : ExtractLineWithoutExtraWhitespaceAtCaret(false)
class CutLineWithoutExtraWhitespaceAtCaretAction : ExtractLineWithoutExtraWhitespaceAtCaret(true)
