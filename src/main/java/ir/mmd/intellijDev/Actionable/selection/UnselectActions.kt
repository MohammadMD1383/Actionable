package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.enableIf

abstract class UnselectAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		getTargetCaret(allCarets).let {
			it.removeSelection()
			caretModel.removeCaret(it)
		}
	}
	
	abstract fun getTargetCaret(carets: List<Caret>): Caret
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor && caretCount > 1 }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

class UnselectFirstSelectionAction : UnselectAction() {
	override fun getTargetCaret(carets: List<Caret>) = carets.first { it.hasSelection() }
}

class UnselectLastSelectionAction : UnselectAction() {
	override fun getTargetCaret(carets: List<Caret>) = carets.last { it.hasSelection() }
}
