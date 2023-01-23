package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class SelectTextUnderCaretAction : AnAction() {
	abstract fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange?
	
	override fun actionPerformed(e: AnActionEvent) = e.editor.allCarets.forEach { caret ->
		val range = getSelectionRange(caret, e.psiFile) ?: return@forEach
		caret.setSelection(range.first, range.last)
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
