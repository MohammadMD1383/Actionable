package ir.mmd.intellijDev.Actionable.psi

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.elementAtOrBefore
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.withEach

abstract class PsiActionAtCaret : AnAction() {
	context (LazyEventContext)
	abstract fun doAction(caret: Caret, psiElement: PsiElement)
	
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		findCaretsAndPsiElements().withEach {
			doAction(first, second)
		}
	}
	
	context (LazyEventContext)
	protected fun findCaretsAndPsiElements() = allCarets.mapNotNull {
		it to (psiFile.elementAtOrBefore(it) ?: return@mapNotNull null)
	}.distinctBy {
		it.second
	}.also { list ->
		list.map {
			it.first
		}.let {
			allCarets.filter { caret ->
				caret !in it
			}.forEach { caret ->
				caretModel.removeCaret(caret)
			}
		}
	}
	
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor }
}

abstract class PsiActionAtCaretWithWriteAction : PsiActionAtCaret() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		runWriteCommandAction {
			findCaretsAndPsiElements().withEach {
				doAction(first, second)
			}
		}
	}
}
