package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction

class DuplicateLineAndInsertContent : MultiCaretAction() {
	context(LazyEventContext) override fun perform(caret: Caret) {
		TODO("Not yet implemented")
	}
}
