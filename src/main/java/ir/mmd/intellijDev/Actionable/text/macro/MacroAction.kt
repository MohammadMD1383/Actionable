package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.util.ext.*

class MacroAction(name: String, private val macro: String) : MultiCaretAction(name) {
	private val macroLength = macro.length
	
	context (LazyEventContext)
	override fun perform(caret: Caret) {
		project.runWriteCommandAction {
			if (caret.hasSelection()) {
				val (start, end) = caret.selectionRangeCompat
				caret.removeSelection()
				document.replaceString(start, end, macro)
				caret moveTo macroLength + start
			} else {
				val offset = caret.offset
				document.insertString(offset, macro)
				caret moveTo macroLength + offset
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
