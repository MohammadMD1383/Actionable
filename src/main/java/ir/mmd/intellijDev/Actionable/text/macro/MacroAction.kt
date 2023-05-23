package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.getWordAtOffset
import ir.mmd.intellijDev.Actionable.util.ext.getWordBoundaries
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import kotlin.collections.component1
import kotlin.collections.component2

class MacroAction(name: String, private val macro: String) : MultiCaretAction(name), DumbAware {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val evaluatedMacro = LazyMacroContext(caret).run { evaluateMacro() }
		
		val replacementStart: Int
		val replacementEnd: Int
		when (evaluatedMacro.replaceTarget) {
			EvaluatedMacro.ReplaceTarget.None -> caret.offset.let {
				replacementStart = it
				replacementEnd = it
			}
			
			EvaluatedMacro.ReplaceTarget.Selection -> {
				replacementStart = caret.selectionStart
				replacementEnd = caret.selectionEnd
				caret.removeSelection()
			}
			
			EvaluatedMacro.ReplaceTarget.Word -> document.getWordBoundaries(caret.offset).let { (start, end) ->
				replacementStart = start
				replacementEnd = end
			}
			
			EvaluatedMacro.ReplaceTarget.Element -> psiFile.elementAt(caret)!!.textRange.let {
				replacementStart = it.startOffset
				replacementEnd = it.endOffset
			}
		}
		
		runWriteCommandAction {
			document.replaceString(replacementStart, replacementEnd, evaluatedMacro.text)
			caret moveTo replacementStart + evaluatedMacro.caretFinalOffset
		}
	}
	
	context (LazyMacroContext)
	private fun evaluateMacro(): EvaluatedMacro { // todo
		var replaceTarget = EvaluatedMacro.ReplaceTarget.None
		var text = macro.replace("""\$([A-Z_]+)\$""".toRegex()) {
			when (it.groupValues[1]) { // todo
				"SELECTION" -> selection after { replaceTarget = EvaluatedMacro.ReplaceTarget.Selection }
				"ELEMENT" -> element after { replaceTarget = EvaluatedMacro.ReplaceTarget.Element }
				"WORD" -> word after { replaceTarget = EvaluatedMacro.ReplaceTarget.Word }
				else -> ""
			}
		}
		
		var finalOffset = 0
		text = text.replace("""\$0\$""".toRegex()) {
			finalOffset = it.range.first
			""
		}
		
		return EvaluatedMacro(text, finalOffset, replaceTarget)
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
	
	context (LazyEventContext)
	private class LazyMacroContext(private val caret: Caret) {
		val selection: String by lazy { selectionModel.selectedText ?: "" }
		val element: String by lazy { psiFile.elementAt(caret)?.text ?: "" }
		val word: String by lazy { document.getWordAtOffset(caret.offset) ?: "" }
	}
	
	private data class EvaluatedMacro(
		val text: String,
		val caretFinalOffset: Int,
		val replaceTarget: ReplaceTarget
	) {
		enum class ReplaceTarget { // todo
			Selection, Word, Element, None
		}
	}
}
