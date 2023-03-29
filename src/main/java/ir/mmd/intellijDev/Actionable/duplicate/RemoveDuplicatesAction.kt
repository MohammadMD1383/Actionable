package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretActionWithInitialization
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState

import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.haveSelection
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction

class RemoveDuplicatesAction : MultiCaretActionWithInitialization<HashSet<String>>() {
	context(LazyEventContext)
	override fun initialize(): HashSet<String> = HashSet()
	
	context (LazyEventContext)
	override fun perform(caret: Caret): Unit = service<SettingsState>().run {
		val text = caret.selectedText!!
		data.find { it.equals(text, ignoreCase = !isCaseSensitive) }?.let {
			project.runWriteCommandAction {
				document.deleteString(caret.selectionStart, caret.selectionEnd)
			}
		} ?: data.add(text)
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor && caretCount > 1 && allCarets.haveSelection }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
