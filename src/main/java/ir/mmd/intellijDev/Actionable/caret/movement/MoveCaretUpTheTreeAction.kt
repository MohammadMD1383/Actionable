package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.editor.ScrollType
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.parentNoWhitespace

class MoveCaretUpTheTreeAction : MultiCaretAction() {
	context (LazyEventContext)
	override fun perform() {
		var element = psiFile.elementAt(caret)?.parentNoWhitespace ?: return
		if (element.textRange.startOffset == caret.offset) {
			element = element.parentNoWhitespace ?: return
		}
		
		caret moveTo element.textRange.startOffset
		scrollingModel.scrollTo(caret.logicalPosition, ScrollType.MAKE_VISIBLE)
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}
