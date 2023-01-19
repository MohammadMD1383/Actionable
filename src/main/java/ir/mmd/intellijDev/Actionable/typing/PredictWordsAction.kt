package ir.mmd.intellijDev.Actionable.typing

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.spellchecker.SpellCheckerManager
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class PredictWordsAction : AnAction() {
	abstract fun transformWords(words: MutableList<String>): String
	
	private fun predictWords(project: Project, text: String) = mutableListOf<String>().apply {
		val length = text.length
		if (length < 4) {
			add(text)
			return@apply
		}
		
		val spellChecker = SpellCheckerManager.getInstance(project)
		var lastWordIndex = 0
		
		loop@ while (true) {
			val delta = length - lastWordIndex
			if (delta < 4) {
				if (delta > 0) add(text.substring(lastWordIndex until length))
				break@loop
			}
			
			for (i in text.indices.reversed()) {
				val word = text.substring(lastWordIndex..i)
				if (!spellChecker.hasProblem(word)) {
					lastWordIndex = i + 1
					add(word)
					continue@loop
				}
			}
		}
	}
	
	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project!!
		val editor = e.editor
		
		project.runWriteCommandAction {
			editor.caretModel.allCarets.forEach { caret ->
				val text = transformWords(predictWords(project, caret.selectedText!!))
				editor.document.replaceString(caret.selectionStart, caret.selectionEnd, text)
				caret.setSelection(caret.selectionStart, caret.selectionStart + text.length)
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasEditorWith { allCaretsHasSelection } }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
