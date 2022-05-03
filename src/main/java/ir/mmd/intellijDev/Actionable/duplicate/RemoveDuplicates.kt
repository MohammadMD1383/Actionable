package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withFindSettings

class RemoveDuplicates : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = withFindSettings {
		val editor = e.editor
		val strings = HashSet<String>()
		
		e.project!!.runWriteCommandActionWith(editor.document) { document ->
			editor.allCarets.forEach { caret ->
				val text = caret.selectedText!!
				strings.find { it.equals(text, ignoreCase = !isCaseSensitive) }?.letOnly {
					document.deleteString(caret.selectionStart, caret.selectionEnd)
				} ?: strings.add(text)
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { caretCount > 1 && allCaretsHasSelection } }
}
