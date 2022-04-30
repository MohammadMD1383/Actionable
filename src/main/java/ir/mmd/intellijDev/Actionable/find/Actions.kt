package ir.mmd.intellijDev.Actionable.find

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.by
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.returnBy
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

abstract class FindAction(private val searchForward: Boolean) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor!!
		val caretModel = editor.caretModel
		val caret = caretModel.allCarets.run { if (searchForward) last() else first() }
		
		/*
		  if there is no selection, first we need to make a selection around the word.
		  this is like what intelliJ does.
		*/
		if (!caret.hasSelection()) return by(MovementSettingsState.getInstance().run { getWordBoundaries(CaretUtil(caret), wordSeparators, hardStopCharacters) }) { (startOffset, endOffset) ->
			if (startOffset != endOffset) caret.setSelection(startOffset, endOffset)
		}
		
		val findResult = FindManager.getInstance(e.project!!).findString(
			editor.document.charsSequence,
			if (searchForward) caret.selectionEnd else caret.selectionStart,
			FindModel().apply {
				isForward = searchForward
				isCaseSensitive = SettingsState.getInstance().isCaseSensitive
				stringToFind = caret.selectedText!!
			}
		)
		
		if (findResult.isStringFound) {
			val position = editor.offsetToVisualPosition(findResult.startOffset + (caret.offset - caret.selectionStart))
			caretModel.addCaret(position, false)?.setSelection(findResult.startOffset, findResult.endOffset)
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}

class SelectPreviousOccurrence : FindAction(false)
class SelectNextOccurrence : FindAction(true)


/* todo: move to another file */

fun Document.getWordBoundaries(
	offset: Int,
	separators: String,
	hardStops: String
): IntArray = returnBy(intArrayOf(0, 0)) {
	return if ((charAtOrNull(offset) ?: return it) in separators + hardStops) it
	else intArrayOf(
		findOffsetOfNext(offset - 1, separators + hardStops, BACKWARD) + 1,
		findOffsetOfNext(offset + 1, separators + hardStops, FORWARD) - 1
	)
}

fun getWordBoundaries( // todo
	cutil: CaretUtil,
	wordSeparators: String,
	hardStops: String
): IntArray {
	cutil.moveUntilReached(wordSeparators, hardStops, BACKWARD)
	val startOffset = cutil.offset
	
	cutil.reset(-1)
	cutil.moveUntilReached(wordSeparators, hardStops, FORWARD)
	cutil.move(+1) // because of the `-1` at upper statement
	
	return intArrayOf(startOffset, cutil.offset)
}
