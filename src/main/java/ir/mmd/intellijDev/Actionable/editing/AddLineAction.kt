package ir.mmd.intellijDev.Actionable.editing

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction

abstract class AddLineAction(private val above: Boolean) : MultiCaretAction(), DumbAware {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val lineNumber = document.getLineNumber(caret.offset)
		
		runWriteCommandAction {
			document.insertString(
				if (above) document.getLineStartOffset(lineNumber) else document.getLineEndOffset(lineNumber),
				"\n"
			)
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class AddLineAboveCaretWMAction : AddLineAction(true)
class AddLineBelowCaretWMAction : AddLineAction(false)
