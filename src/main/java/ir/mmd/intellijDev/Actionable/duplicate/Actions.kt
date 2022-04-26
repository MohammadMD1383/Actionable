package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

abstract class DuplicateAction(private val duplicator: DuplicateUtil.(Int, Int) -> Unit) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val dutil = DuplicateUtil(editor)
		
		editor.caretModel.allCarets.forEach {
			dutil.duplicator(it.selectionStart, it.selectionEnd)
		}
	}
	
	override fun update(e: AnActionEvent) {
		val project = e.project
		val editor = e.getData(CommonDataKeys.EDITOR)
		e.presentation.isEnabled = project != null && editor != null
	}
}

class DuplicateLinesUp : DuplicateAction(DuplicateUtil::duplicateUp)
class DuplicateLinesDown : DuplicateAction(DuplicateUtil::duplicateDown)
