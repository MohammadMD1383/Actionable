package ir.mmd.intellijDev.Actionable.caret

import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.dontHaveSelection

abstract class ActionAtCaret<TModel : ActionAtCaret.Model, TKey>(private val inWriteAction: Boolean = false) : ActionBase() {
	open class Model(
		val caret: Caret
	)
	
	context (LazyEventContext)
	protected abstract fun doAction(model: TModel)
	
	context (LazyEventContext)
	protected abstract fun createModel(caret: Caret): TModel?
	
	protected abstract fun distinctKey(model: TModel): TKey
	
	context (LazyEventContext)
	final override fun performAction() {
		allCarets.mapNotNull {
			createModel(it)
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
		}.forEach {
			if (inWriteAction) {
				runWriteCommandAction { doAction(it) }
			} else {
				doAction(it)
			}
		}
	}
	
	context(LazyEventContext)
	override fun isEnabled() = hasEditor and allCarets.dontHaveSelection
}
