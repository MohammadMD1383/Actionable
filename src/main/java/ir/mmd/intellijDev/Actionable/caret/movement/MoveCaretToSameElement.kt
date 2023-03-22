package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.service

abstract class MoveCaretToSameElement : MultiCaretAction() {
	protected abstract fun PsiElement.getNextLeafElement(): PsiElement?
	
	context(LazyEventContext)
	override fun perform(caret: Caret) {
		val element = psiFile.elementAt(caret) ?: return
		var targetElement = element.getNextLeafElement()
		
		while (targetElement != null && targetElement.elementType != element.elementType) {
			targetElement = targetElement.getNextLeafElement()
		}
		
		targetElement ?: return
		
		val targetOffset: Int
		val targetElementTextRange = targetElement.textRange
		
		targetOffset = when (service<SettingsState>().sameElementMovementBehaviour) {
			SettingsState.SEMBehaviour.Start -> targetElementTextRange.startOffset
			SettingsState.SEMBehaviour.End -> targetElementTextRange.endOffset
			
			SettingsState.SEMBehaviour.Offset -> {
				val caretOffset = caret.offset
				val offset = caretOffset - element.textRange.startOffset
				val finalOffset = targetElementTextRange.startOffset + offset
				
				if (finalOffset > targetElementTextRange.endOffset) targetElementTextRange.endOffset else finalOffset
			}
		}
		
		caret moveTo targetOffset
	}
	
	override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}
