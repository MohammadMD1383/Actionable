package ir.mmd.intellijDev.Actionable.caret

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.dontHaveSelection
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.withEach

abstract class ActionAtCaret<TModel : ActionAtCaret.IModel, TKey>(private val inWriteAction: Boolean = false) : AnAction() {
	interface IModel {
		val caret: Caret
	}
	
	context (LazyEventContext)
	protected abstract fun doAction(model: TModel)
	
	context (LazyEventContext)
	protected abstract fun mapCaret(caret: Caret): TModel?
	
	protected abstract fun distinctKey(model: TModel): TKey
	
	final override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		allCarets.mapNotNull {
			mapCaret(it)
		}.distinctBy {
			distinctKey(it)
		}.also {
			it.map { model ->
				model.caret
			}.let { list ->
				allCarets.filter { caret ->
					caret !in list
				}.forEach { caret ->
					caretModel.removeCaret(caret)
				}
			}
		}.withEach {
			if (inWriteAction) {
				runWriteCommandAction { doAction(this) }
			} else {
				doAction(this)
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor and allCarets.dontHaveSelection }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
