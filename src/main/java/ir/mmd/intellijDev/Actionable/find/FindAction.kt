package ir.mmd.intellijDev.Actionable.find

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.by
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.service
import ir.mmd.intellijDev.Actionable.util.withService
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

abstract class FindAction(private val searchForward: Boolean) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = withService<MovementSettingsState, Unit> {
		val editor = e.editor
		val caretModel = editor.caretModel
		val caret = caretModel.allCarets.run { if (searchForward) last() else first() }
		
		if (!caret.hasSelection()) return by(caret.util.getWordBoundaries(wordSeparators, hardStopCharacters)) { (startOffset, endOffset) ->
			if (startOffset != endOffset) caret.setSelection(startOffset, endOffset)
		}
		
		val (found, startOffset, endOffset) = FindManager.getInstance(e.project!!).findString(
			editor.document.charsSequence,
			if (searchForward) caret.selectionEnd else caret.selectionStart,
			FindModel().apply {
				isForward = searchForward
				isCaseSensitive = service<SettingsState>().isCaseSensitive
				stringToFind = caret.selectedText!!
			}
		)
		
		if (found) caretModel.addCaret(
			editor.offsetToVisualPosition(startOffset + (caret.offset - caret.selectionStart)),
			false
		)!!.setSelection(startOffset, endOffset)
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}
