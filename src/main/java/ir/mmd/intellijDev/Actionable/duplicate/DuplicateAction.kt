package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.DuplicateUtil
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class DuplicateAction(private val duplicate: DuplicateUtil.(Int, Int) -> Unit) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val duplicator = editor.duplicator
		
		editor.caretModel.allCarets.withEach {
			duplicator.duplicate(selectionStart, selectionEnd)
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}
