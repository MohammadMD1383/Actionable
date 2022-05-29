package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.*

class MacroAction(name: String, private val macro: String) : AnAction(name) {
	private val macroLength = macro.length
	
	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project!!
		val editor = e.editor
		
		project.runWriteCommandActionWith(editor.document) { document ->
			editor.allCarets.forEach { caret ->
				val offset = caret.offset
				document.insertString(offset, macro)
				caret moveTo offset + macroLength
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor }
}