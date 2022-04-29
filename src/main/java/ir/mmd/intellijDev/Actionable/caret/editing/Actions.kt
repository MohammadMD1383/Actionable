package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseMotionListener
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.util.Key
import ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil
import ir.mmd.intellijDev.Actionable.find.getWordBoundaries
import ir.mmd.intellijDev.Actionable.util.*
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

abstract class EditingAction : AnAction() {
	companion object {
		@JvmStatic
		protected val scheduledPasteActionKind = Key<String>("scheduledPasteAction.kink")
		
		@JvmStatic
		protected val scheduledPasteActionOffset = Key<Int>("scheduledPasteAction.offset")
		
		@JvmStatic
		protected val previousHighlighterKey = Key<RangeHighlighter>("scheduledPasteAction.motionListener")
		
		/**
		 * removes the `scheduledPasteAction` from the given editor
		 *
		 * @param editor the editor to remove paste action from
		 */
		fun removeScheduledPasteAction(editor: Editor) {
			if (SettingsState.getInstance().showPasteActionHints) {
				editor.removeEditorMouseMotionListener(motionListener)
				editor.getUserData(previousHighlighterKey)?.let { editor.markupModel.removeHighlighter(it) }
				editor.putUserData(previousHighlighterKey, null)
			}
			
			editor.putUserData(scheduledPasteActionKind, null)
			editor.putUserData(scheduledPasteActionOffset, null)
		}
		
		/**
		 * this is used to highlight the word/element which is under the caret when there is an active `scheduledPasteAction`
		 */
		private val motionListener: EditorMouseMotionListener = object : EditorMouseMotionListener {
			override fun mouseMoved(e: EditorMouseEvent) {
				val editor = e.editor
				val previousHighlighter = editor.getUserData(previousHighlighterKey)
				val offset = editor.run { logicalPositionToOffset(xyToLogicalPosition(e.mouseEvent.point)) }
				if (previousHighlighter?.run { offset in startOffset..endOffset } == true) return
				
				val startOffset: Int
				val endOffset: Int
				
				if (editor.getUserData(scheduledPasteActionKind)!!.split(';')[0] == "el") {
					val element = editor.project!!.psiFileFor(editor.document)?.findElementAt(offset)
					if (element?.text?.isBlank() == true) {
						startOffset = 0
						endOffset = 0
					} else element!!.textRange.let {
						startOffset = it.startOffset
						endOffset = it.endOffset
					}
				} else  /* isWordTarget */ {
					val wordBoundaries = intArrayOf(0, 0)
					Actions.getWordAtOffset(editor.document, offset, wordBoundaries)
					startOffset = wordBoundaries[0]
					endOffset = wordBoundaries[1]
				}
				
				editor.markupModel.runOnly {
					previousHighlighter?.let { removeHighlighter(it) }
					editor.putUserData(previousHighlighterKey, addRangeHighlighter(
						startOffset,
						endOffset,
						HighlighterLayer.LAST + 10,
						EditorColors.IDENTIFIER_UNDER_CARET_ATTRIBUTES.defaultAttributes,
						HighlighterTargetArea.EXACT_RANGE
					))
				}
			}
			
			/**
			 * implemented to be compatible with earlier versions of the intellij idea
			 */
			override fun mouseDragged(e: EditorMouseEvent) {}
		}
		
	}
	
	override fun update(e: AnActionEvent) {
		val project = e.project
		val editor = e.getData(CommonDataKeys.EDITOR)
		e.presentation.isEnabled = project != null && editor != null && editor.caretModel.caretCount == 1
	}
	
	/**
	 * implementation of:
	 *
	 *  * [CopyElementAtCaret]
	 *  * [CutElementAtCaret]
	 *
	 *
	 * @param e             instance of [AnActionEvent]
	 * @param deleteElement if true, element will be deleted after being copied,
	 * in other words it cuts the element
	 */
	fun copyElementAtCaret(
		e: AnActionEvent,
		deleteElement: Boolean
	) {
		e.psiFile!!.elementAt(e.primaryCaret!!)?.runOnly {
			text.copyToClipboard()
			if (deleteElement) e.project!!.runWriteCommandAction(::delete)
		}
	}
	
	/**
	 * implementation of:
	 *
	 *  * [CopyWordAtCaret]
	 *  * [CutWordAtCaret]
	 *
	 *
	 * @param e          instance of [AnActionEvent]
	 * @param deleteWord if true, word will be deleted after being copied,
	 * in other words it cuts the word
	 */
	fun copyWordAtCaret(
		e: AnActionEvent,
		deleteWord: Boolean
	) {
		val document = e.editor!!.document
		val wordBoundaries = IntArray(2)
		Actions.getWordAtCaret(document, e.primaryCaret!!, wordBoundaries)?.let {
			it.copyToClipboard()
			if (deleteWord) e.project!!.runWriteCommandAction { document.deleteString(wordBoundaries[0], wordBoundaries[1]) }
		}
	}
	
	/**
	 * implementation of:
	 *
	 *  * [SetElementCopyPasteOffset]
	 *  * [SetElementCutPasteOffset]
	 *  * [SetWordCopyPasteOffset]
	 *  * [SetWordCutPasteOffset]
	 *
	 *
	 * @param e          instance of [AnActionEvent]
	 * @param actionName the action command; see usages for more info
	 */
	fun setPasteOffset(
		e: AnActionEvent,
		actionName: String
	) {
		val editor = e.editor!!
		val caret = editor.caretModel.primaryCaret
		
		if (actionName == editor.getUserData(scheduledPasteActionKind)) {
			if (editor.getUserData(scheduledPasteActionOffset) == caret.offset) {
				removeScheduledPasteAction(editor)
			} else  /* offset != caret.offset */ {
				editor.putUserData(scheduledPasteActionOffset, caret.offset)
			}
			return
		}
		
		editor.putUserData(scheduledPasteActionKind, actionName)
		editor.putUserData(scheduledPasteActionOffset, caret.offset)
		if (SettingsState.getInstance().showPasteActionHints) editor.addEditorMouseMotionListener(motionListener)
	}
}


class CutWordAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, true)
}

class CutElementAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, true)
}

class CopyWordAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, false)
}

class CopyElementAtCaret : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, false)
}

class SetWordCutPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;ct")
}

class SetWordCopyPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;cp")
}

class SetElementCutPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;ct")
}

class SetElementCopyPasteOffset : EditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;cp")
}


object Actions {
	/**
	 * returns the word at the specified offset in the given document
	 *
	 * @param document instance of [Document]
	 * @param offset   offset in the document to find word at
	 * @param wb       (optional) an IntArray(2) to retrieve word boundaries
	 * @return the word
	 */
	fun getWordAtOffset(
		document: Document,
		offset: Int,
		wb: IntArray?
	): String? {
		val documentChars = document.charsSequence
		val settingsState = MovementSettingsState.getInstance()
		val wordSeparators = settingsState.wordSeparators
		val hardStopCharacters = settingsState.hardStopCharacters
		val wordBoundaries = getWordBoundaries(document, wordSeparators, hardStopCharacters, offset)
		return if (wordBoundaries[0] != wordBoundaries[1]) {
			/* check if the caller wants the word boundaries */
			wb?.let {
				it[0] = wordBoundaries[0]
				it[1] = wordBoundaries[1]
			}
			
			val addition = if (documentChars[offset] !in wordSeparators + hardStopCharacters) +1 else 0
			documentChars.substring(wordBoundaries[0], wordBoundaries[1] + addition)
		} else null
	}
	
	/**
	 * returns the word located at the caret
	 *
	 * @param document instance of the [Document]
	 * @param caret    the [Caret] to find the word at
	 * @param wb       (optional) array of two elements that will be filled with the word boundaries
	 * found at the caret
	 * @return a string containing the word or null if the word is not matched
	 */
	fun getWordAtCaret(
		document: Document,
		caret: Caret,
		wb: IntArray?
	): String? {
		val documentChars = document.charsSequence
		val settingsState = MovementSettingsState.getInstance()
		val wordSeparators = settingsState.wordSeparators
		val hardStopCharacters = settingsState.hardStopCharacters
		val cutil = CaretUtil(caret)
		val wordBoundaries = getWordBoundaries(cutil, wordSeparators, hardStopCharacters)
		return if (wordBoundaries[0] != wordBoundaries[1]) {
			/* check if the caller wants the word boundaries */
			wb?.let {
				wb[0] = wordBoundaries[0]
				wb[1] = wordBoundaries[1]
			}
			
			val addition = if (cutil.peek(0)!! !in wordSeparators + hardStopCharacters) +1 else 0
			documentChars.substring(wordBoundaries[0], wordBoundaries[1] + addition)
		} else null
	}
}
