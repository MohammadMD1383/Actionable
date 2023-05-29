package ir.mmd.intellijDev.Actionable.caret

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.getWordAtOffset

abstract class WordActionAtCaret(
	removeCarets: Boolean = true,
	inWriteAction: Boolean = false
) : ActionAtCaret<WordActionAtCaret.Model>(removeCarets, inWriteAction), DumbAware {
	class Model(
		caret: Caret,
		val word: String,
		val boundaries: IntArray
	) : ActionAtCaret.Model(caret)
	
	context(LazyEventContext)
	override fun createModel(caret: Caret): Model? {
		val boundaries = IntArray(2)
		val word = document.getWordAtOffset(caret.offset, boundaries) ?: return null
		return Model(caret, word, boundaries)
	}
	
	override fun distinctKey(model: Model) = model.word
}
