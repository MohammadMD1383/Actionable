package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext

abstract class UnselectAction : ActionBase(), DumbAware {
	abstract fun getTargetCaret(carets: List<Caret>): Caret
	
	context (LazyEventContext)
	override fun performAction() {
		getTargetCaret(allCarets).let(caretModel::removeCaret)
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && caretCount > 1
}

class UnselectFirstSelectionAction : UnselectAction() {
	override fun getTargetCaret(carets: List<Caret>) = carets.first { it.hasSelection() }
}

class UnselectLastSelectionAction : UnselectAction() {
	override fun getTargetCaret(carets: List<Caret>) = carets.last { it.hasSelection() }
}
