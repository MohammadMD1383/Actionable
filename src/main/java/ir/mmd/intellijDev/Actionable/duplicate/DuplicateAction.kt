package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretActionWithInitialization
import ir.mmd.intellijDev.Actionable.util.DuplicateUtil
import ir.mmd.intellijDev.Actionable.util.ext.duplicator
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasEditor
import ir.mmd.intellijDev.Actionable.util.ext.hasProject

abstract class DuplicateAction(private val duplicate: DuplicateUtil.(Int, Int) -> Unit) : MultiCaretActionWithInitialization<DuplicateUtil>() {
	context (LazyEventContext)
	override fun initialize() = editor.duplicator
	
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		data.duplicate(caret.selectionStart, caret.selectionEnd)
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
