package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withService

@Keep
class RemoveDuplicates : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = withService<SettingsState, Unit> {
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
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { caretCount > 1 && allCaretsHasSelection } }
}
