package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.*

class MacroAction(name: String, private val macro: String) : AnAction(name) {
	private val macroLength = macro.length
	
	override fun actionPerformed(e: AnActionEvent) = with(e.editor) {
		e.project!!.runWriteCommandActionWith(document) { document ->
			allCarets.forEach { caret ->
				caret moveTo macroLength + if (caret.hasSelection()) {
					val (start, end) = caret.selectionRangeCompat
					caret.removeSelection()
					document.replaceString(start, end, macro)
					start
				} else {
					val offset = caret.offset
					document.insertString(offset, macro)
					offset
				}
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
