package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasEditor
import ir.mmd.intellijDev.Actionable.util.ext.hasProject

abstract class SelectTextUnderCaretAction : MultiCaretAction() {
	abstract fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange?
	
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val range = getSelectionRange(caret, psiFile) ?: return
		caret.setSelection(range.first, range.last)
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
