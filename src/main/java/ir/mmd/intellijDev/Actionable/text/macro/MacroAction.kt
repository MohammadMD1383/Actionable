package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.*

class MacroAction(name: String, private val macro: String) : MultiCaretAction(name) {
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		val evaluatedMacro = LazyMacroContext(caret).run { evaluateMacro() }
		val selectionStart = caret.selectionStart
		
		project.runWriteCommandAction {
			document.replaceString(selectionStart, caret.selectionEnd, evaluatedMacro.text)
			caret.removeSelection()
			caret moveTo selectionStart + evaluatedMacro.caretFinalOffset
		}
	}
	
	context (LazyMacroContext)
	private fun evaluateMacro(): EvaluatedMacro {
		var text = macro.replace("""\$([A-Z_]+)\$""".toRegex()) {
			when (it.groupValues[1]) {
				"SELECTION" -> selection
				"ELEMENT" -> element
				"WORD" -> word
				else -> ""
			}
		}
		
		var finalOffset = 0
		text = text.replace("""\$0\$""".toRegex()) {
			finalOffset = it.range.first
			""
		}
		
		return EvaluatedMacro(text, finalOffset)
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	
	context (LazyEventContext)
	private class LazyMacroContext(private val caret: Caret) {
		val selection: String by lazy { selectionModel.selectedText ?: "" }
		val element: String by lazy { psiFile.elementAt(caret)?.text ?: "" }
		val word: String by lazy { document.getWordAtOffset(caret.offset) ?: "" }
	}
	
	private data class EvaluatedMacro(
		val text: String,
		val caretFinalOffset: Int
	)
}
