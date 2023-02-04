package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.*

class MoveCaretUpTheTreeAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val psiFile = e.psiFile
		
		e.allCarets.forEach { caret ->
			var element = psiFile.elementAt(caret)?.parentNoWhitespace ?: return@forEach
			if (element.textRange.startOffset == caret.offset) {
				element = element.parentNoWhitespace ?: return@forEach
			}
			
			caret moveTo element.textRange.startOffset
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
