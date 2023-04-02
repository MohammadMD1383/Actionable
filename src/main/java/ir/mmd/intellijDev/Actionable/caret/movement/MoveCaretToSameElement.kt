package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.nextLeafNoWhitespace
import ir.mmd.intellijDev.Actionable.util.ext.prevLeafNoWhitespace

abstract class MoveCaretToSameElement(private val forward: Boolean) : ActionBase() {
	private val getNextLeafElement = if (forward) PsiElement::nextLeafNoWhitespace else PsiElement::prevLeafNoWhitespace
	
	context (LazyEventContext)
	override fun performAction() {
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
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class MoveCaretToNextSameElement : MoveCaretToSameElement(true)
class MoveCaretToPreviousSameElement : MoveCaretToSameElement(false)
