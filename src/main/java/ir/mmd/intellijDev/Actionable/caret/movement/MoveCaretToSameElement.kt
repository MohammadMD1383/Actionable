package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class MoveCaretToSameElement(private val forward: Boolean) : AnAction() {
	private val getNextLeafElement = if (forward) PsiElement::nextLeafNoWhitespace else PsiElement::prevLeafNoWhitespace
	
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		allCarets.forEach { caret ->
			if (!caret.isValid) {
				return@forEach
			}
			
			val element = psiFile.elementAt(caret) ?: return@forEach
			var targetElement = getNextLeafElement(element, false)
			
			while (targetElement != null && targetElement.elementType != element.elementType) {
				targetElement = getNextLeafElement(targetElement, false)
			}
			
			targetElement ?: return@forEach
			
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
		
		scrollingModel.scrollTo(
			(if (forward) allCarets.last { it.isValid } else allCarets.first { it.isValid }).logicalPosition,
			ScrollType.MAKE_VISIBLE
		)
	}
	
	override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}
