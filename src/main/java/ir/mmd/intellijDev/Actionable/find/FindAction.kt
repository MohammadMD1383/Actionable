package ir.mmd.intellijDev.Actionable.find

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.afterDoing
import ir.mmd.intellijDev.Actionable.util.ext.component1
import ir.mmd.intellijDev.Actionable.util.ext.component2
import ir.mmd.intellijDev.Actionable.util.ext.component3
import ir.mmd.intellijDev.Actionable.util.ext.util
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

abstract class FindAction(private val searchForward: Boolean) : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() {
		val settings = service<MovementSettingsState>()
		val caret = allCarets.run { if (searchForward) last() else first() }
		
		if (!caret.hasSelection()) return afterDoing {
			val (startOffset, endOffset) = caret.util.getWordBoundaries(settings.wordSeparators, settings.hardStopCharacters)
			
			if (startOffset != endOffset) {
				caret.setSelection(startOffset, endOffset)
			}
		}
		
		val (found, startOffset, endOffset) = FindManager.getInstance(project).findString(
			document.charsSequence,
			if (searchForward) caret.selectionEnd else caret.selectionStart,
			FindModel().apply {
				isForward = searchForward
				isCaseSensitive = service<SettingsState>().isCaseSensitive
				stringToFind = caret.selectedText!!
			}
		)
		
		if (found) {
			val visualPosition = editor.offsetToVisualPosition(startOffset + (caret.offset - caret.selectionStart))
			caretModel.addCaret(visualPosition, false)!!.setSelection(startOffset, endOffset)
			scrollingModel.scrollTo(editor.visualToLogicalPosition(visualPosition), ScrollType.RELATIVE)
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class SelectPreviousOccurrence : FindAction(false)
class SelectNextOccurrence : FindAction(true)
