package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class SelectTextUnderCaretAction : AnAction() {
	abstract fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange?
	
	override fun actionPerformed(e: AnActionEvent) = e.editor.allCarets.forEach { caret ->
		if (caret.hasSelection()) return@forEach
		caret.setSelection(getSelectionRange(caret, e.psiFile) ?: return@forEach)
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}
