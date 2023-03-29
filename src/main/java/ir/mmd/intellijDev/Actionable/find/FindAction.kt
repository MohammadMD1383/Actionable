package ir.mmd.intellijDev.Actionable.find

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.afterDoing
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

abstract class FindAction(private val searchForward: Boolean) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
		service<MovementSettingsState>().run {
			val caret = allCarets.run { if (searchForward) last() else first() }
			
			if (!caret.hasSelection()) return afterDoing {
				val (startOffset, endOffset) = caret.util.getWordBoundaries(wordSeparators, hardStopCharacters)
				
				if (startOffset != endOffset) {
					caret.setSelection(startOffset, endOffset)
				}
			}
			
			val (found, startOffset, endOffset) = FindManager.getInstance(e.project!!).findString(
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
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
