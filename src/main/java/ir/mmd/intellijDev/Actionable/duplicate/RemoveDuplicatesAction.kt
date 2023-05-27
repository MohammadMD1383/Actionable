package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.haveSelection

class RemoveDuplicatesAction : MultiCaretAction(), DumbAware {
	private var set: HashSet<String>? = null
	
	context(LazyEventContext)
	override fun initialize() {
		set = hashSetOf()
	}
	
	context(LazyEventContext)
	override fun finalize() {
		set = null
	}
	
	context (LazyEventContext)
	override fun perform(): Unit = service<SettingsState>().run {
		val text = caret.selectedText!!
		set!!.find { it.equals(text, ignoreCase = !isCaseSensitive) }?.let {
			runWriteCommandAction {
				document.deleteString(caret.selectionStart, caret.selectionEnd)
			}
		} ?: set!!.add(text)
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && caretCount > 1 && allCarets.haveSelection
}
