package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasEditor
import ir.mmd.intellijDev.Actionable.util.ext.hasProject

abstract class DuplicateAction(private val duplicator: DuplicateUtil.(Int, Int) -> Unit) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor!!
		val dutil = DuplicateUtil(editor)
		
		editor.caretModel.allCarets.forEach {
			dutil.duplicator(it.selectionStart, it.selectionEnd)
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}

class DuplicateLinesUp : DuplicateAction(DuplicateUtil::duplicateUp)
class DuplicateLinesDown : DuplicateAction(DuplicateUtil::duplicateDown)
