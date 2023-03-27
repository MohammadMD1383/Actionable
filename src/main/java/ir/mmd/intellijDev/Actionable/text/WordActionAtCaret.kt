package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.caret.ActionAtCaret
import ir.mmd.intellijDev.Actionable.util.ext.getWordAtOffsetOrBefore

abstract class WordActionAtCaret(inWriteAction: Boolean = false) : ActionAtCaret<WordActionAtCaret.Model, String>(inWriteAction) {
	class Model(
		override val caret: Caret,
		val word: String,
		val boundaries: IntArray
	) : IModel
	
	context(LazyEventContext)
	override fun mapCaret(caret: Caret): Model? {
		val boundaries = IntArray(2)
		val word = document.getWordAtOffsetOrBefore(caret.offset, boundaries) ?: return null
		return Model(caret, word, boundaries)
	}
	
	override fun distinctKey(model: Model) = model.word
	
	override fun isDumbAware() = true
}
