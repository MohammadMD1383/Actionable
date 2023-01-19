package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.caretCount
import ir.mmd.intellijDev.Actionable.util.ext.editor
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasEditorWith

abstract class UnselectAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val caretModel = e.editor.caretModel
		getTargetCaret(caretModel.allCarets).run {
			removeSelection()
			caretModel.removeCaret(this)
		}
	}
	
	abstract fun getTargetCaret(carets: List<Caret>): Caret
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasEditorWith { caretCount > 1 } }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

@Keep
class UnselectFirstSelectionAction : UnselectAction() {
	override fun getTargetCaret(carets: List<Caret>) = carets.first { it.hasSelection() }
}

@Keep
class UnselectLastSelectionAction : UnselectAction() {
	override fun getTargetCaret(carets: List<Caret>) = carets.last { it.hasSelection() }
}
