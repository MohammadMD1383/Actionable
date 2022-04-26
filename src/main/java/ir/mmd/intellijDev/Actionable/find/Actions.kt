package ir.mmd.intellijDev.Actionable.find

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementUtil
import ir.mmd.intellijDev.Actionable.caret.movement.OffsetMovementUtil
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState as FindSettingsState

abstract class FindAction(private val searchForward: Boolean) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project!!
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val document = editor.document
		val caretModel = editor.caretModel
		val caret = if (searchForward) caretModel.allCarets.last() else caretModel.allCarets.first()
		val caretMovementSettingsState = SettingsState.getInstance()
		val searchCaseSensitive = FindSettingsState.getInstance().isCaseSensitive
		val wordSeparators = caretMovementSettingsState.wordSeparators
		val hardStopCharacters = caretMovementSettingsState.hardStopCharacters
		
		/*
		  if there is no selection, first we need to make a selection around the word.
		  this is like what intelliJ does.
		*/
		if (!caret.hasSelection()) {
			val cutil = CaretMovementUtil(caret)
			val wordBoundaries = getWordBoundaries(cutil, wordSeparators, hardStopCharacters)
			
			/*
			  wordBoundaries[0]: startOffset
			  wordBoundaries[1]: endOffset
			  startOffset == endOffset : there is no word around caret
			*/
			if (wordBoundaries[0] != wordBoundaries[1]) {
				// final Character endChar = cutil.peek(0);
				// final int addition = !wordSeparators.contains(endChar.toString()) ? +1 : 0;
				caret.setSelection(wordBoundaries[0], wordBoundaries[1] /*+ addition*/)
			}
			return
		}
		
		val findModel = FindModel().apply {
			isForward = searchForward
			isCaseSensitive = searchCaseSensitive
			stringToFind = caret.selectedText!!
		}
		val findManager = FindManager.getInstance(project)
		val findResult = findManager.findString(
			document.charsSequence,
			if (searchForward) caret.selectionEnd else caret.selectionStart,
			findModel
		)
		
		if (findResult.isStringFound) {
			val position = editor.offsetToVisualPosition(findResult.startOffset + (caret.offset - caret.selectionStart))
			val newCaret = caretModel.addCaret(position, true)
			newCaret?.setSelection(findResult.startOffset, findResult.endOffset)
		}
	}
	
	override fun update(e: AnActionEvent) {
		val project = e.project
		val editor = e.getData(CommonDataKeys.EDITOR)
		e.presentation.isEnabled = project != null && editor != null
	}
}

class SelectPreviousOccurrence : FindAction(false)
class SelectNextOccurrence : FindAction(true)



/* todo: move to another file */

/**
 * returns the start and end offset of the word which is located at the specified offset in the given document (aka: word boundaries)
 *
 * @param document       instance of [Document]
 * @param wordSeparators a string containing the word separators
 * @param hardStops      a string containing the hard stop word separators
 * @param offset         the offset in the document to find word boundaries at
 * @return word boundaries or null if not found
 */
fun getWordBoundaries(
	document: Document,
	wordSeparators: String,
	hardStops: String,
	offset: Int
): IntArray {
	val startOffset = OffsetMovementUtil.goUntilReached(document, wordSeparators, hardStops, offset, CaretMovementHelper.BACKWARD)
	val endOffset = OffsetMovementUtil.goUntilReached(document, wordSeparators, hardStops, offset - 1, CaretMovementHelper.FORWARD) + 1
	return intArrayOf(startOffset, endOffset)
}

/**
 * returns the start and end offset of the word which is located at the specified caret (aka: word boundaries)
 *
 * @param cutil          instance of [CaretMovementUtil] containing the specified caret
 * @param wordSeparators a string containing the word separators
 * @param hardStops      a string containing the hard stop word separators
 * @return an `int[2]` containing the boundaries
 */
fun getWordBoundaries(
	cutil: CaretMovementUtil,
	wordSeparators: String,
	hardStops: String
): IntArray {
	CaretMovementHelper.goUntilReached(cutil, wordSeparators, hardStops, CaretMovementHelper.BACKWARD)
	val startOffset = cutil.offset
	
	/*
	  because `goUntilReached` method will look for the next character relative to the character associated with the caret,
	  not visually the character after caret.
	  visually the character after caret, is actually the character that is associated with the caret.
	*/
	cutil.reset(-1)
	CaretMovementHelper.goUntilReached(cutil, wordSeparators, hardStops, CaretMovementHelper.FORWARD)
	cutil.go(+1) // because of the `-1` at upper statement
	val endOffset = cutil.offset
	return intArrayOf(startOffset, endOffset)
}
