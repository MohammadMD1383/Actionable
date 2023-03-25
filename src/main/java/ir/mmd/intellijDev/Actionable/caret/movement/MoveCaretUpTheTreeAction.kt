package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.ScrollType
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.parentNoWhitespace

class MoveCaretUpTheTreeAction : MultiCaretAction() {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		var element = psiFile.elementAt(caret)?.parentNoWhitespace ?: return
		if (element.textRange.startOffset == caret.offset) {
			element = element.parentNoWhitespace ?: return
		}
		
		caret moveTo element.textRange.startOffset
		scrollingModel.scrollTo(caret.logicalPosition, ScrollType.MAKE_VISIBLE)
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
