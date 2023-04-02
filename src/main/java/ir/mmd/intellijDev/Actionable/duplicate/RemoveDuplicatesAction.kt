package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretActionWithInitialization
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.haveSelection

class RemoveDuplicatesAction : MultiCaretActionWithInitialization<HashSet<String>>(), DumbAware {
	context(LazyEventContext)
	override fun initialize(): HashSet<String> = HashSet()
	
	context (LazyEventContext)
	override fun perform(caret: Caret): Unit = service<SettingsState>().run {
		val text = caret.selectedText!!
		data.find { it.equals(text, ignoreCase = !isCaseSensitive) }?.let {
			runWriteCommandAction {
				document.deleteString(caret.selectionStart, caret.selectionEnd)
			}
		} ?: data.add(text)
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && caretCount > 1 && allCarets.haveSelection
}
