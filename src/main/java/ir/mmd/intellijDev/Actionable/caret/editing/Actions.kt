package ir.mmd.intellijDev.Actionable.caret.editing

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
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementUtil
import ir.mmd.intellijDev.Actionable.find.getWordBoundaries
import ir.mmd.intellijDev.Actionable.util.copyToClipboard
import ir.mmd.intellijDev.Actionable.util.runWriteCommandAction
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

/**
 * This class contains implementation for these actions:
 *
 *  * [CopyElementAtCaret]
 *  * [CutElementAtCaret]
 *  * [CopyWordAtCaret]
 *  * [CutWordAtCaret]
 *  * [SetElementCopyPasteOffset]
 *  * [SetElementCutPasteOffset]
 *  * [SetWordCopyPasteOffset]
 *  * [SetWordCutPasteOffset]
 *
 */
object Actions {
	@JvmField
	val scheduledPasteActionKind = Key<String>("scheduledPasteAction.kink")
	
	@JvmField
	val scheduledPasteActionOffset = Key<Int>("scheduledPasteAction.offset")
	
	@JvmField
	val previousHighlighterKey = Key<RangeHighlighter>("scheduledPasteAction.motionListener")
	
	/**
	 * common action availability criteria among these actions:
	 *
	 *  * [CopyElementAtCaret]
	 *  * [CutElementAtCaret]
	 *  * [CopyWordAtCaret]
	 *  * [CutWordAtCaret]
	 *  * [SetElementCopyPasteOffset]
	 *  * [SetElementCutPasteOffset]
	 *  * [SetWordCopyPasteOffset]
	 *  * [SetWordCutPasteOffset]
	 *
	 *
	 * @param e event of execution
	 */
	fun setActionAvailability(e: AnActionEvent) {
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
		val project = e.project!!
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		getElementAtCaret(project, document, caret)?.let {
			it.text.copyToClipboard()
			if (deleteElement) project.runWriteCommandAction { it.delete() }
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
		val project = e.project!!
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		val wordBoundaries = IntArray(2)
		getWordAtCaret(document, caret, wordBoundaries)?.let {
			it.copyToClipboard()
			if (deleteWord) project.runWriteCommandAction { document.deleteString(wordBoundaries[0], wordBoundaries[1]) }
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
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		val caret = editor.caretModel.primaryCaret
		val showPasteActionHints = SettingsState.getInstance().showPasteActionHints
		val kind = editor.getUserData(scheduledPasteActionKind)
		val offset = editor.getUserData(scheduledPasteActionOffset)
		
		if (actionName == kind) {
			if (offset == caret.offset) {
				removeScheduledPasteAction(editor)
			} else  /* offset != caret.offset */ {
				editor.putUserData(scheduledPasteActionOffset, caret.offset)
			}
			return
		}
		
		editor.putUserData(scheduledPasteActionKind, actionName)
		editor.putUserData(scheduledPasteActionOffset, caret.offset)
		if (showPasteActionHints) editor.addEditorMouseMotionListener(motionListener)
	}
	
	/**
	 * removes the `scheduledPasteAction` from the given editor
	 *
	 * @param editor the editor to remove paste action from
	 */
	@JvmStatic
	fun removeScheduledPasteAction(editor: Editor) {
		if (SettingsState.getInstance().showPasteActionHints) {
			editor.removeEditorMouseMotionListener(motionListener)
			val rangeHighlighter = editor.getUserData(previousHighlighterKey)
			if (rangeHighlighter != null) editor.markupModel.removeHighlighter(rangeHighlighter)
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
			val offset = editor.logicalPositionToOffset(editor.xyToLogicalPosition(e.mouseEvent.point))
			if (previousHighlighter == null || offset !in previousHighlighter.startOffset..previousHighlighter.endOffset) {
				val project = editor.project!!
				val document = editor.document
				val markupModel = editor.markupModel
				val pasteKind = editor.getUserData(scheduledPasteActionKind)
				val isElementTarget = pasteKind!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] == "el"
				val startOffset: Int
				val endOffset: Int
				if (isElementTarget) {
					val element = getElementAtOffset(project, document, offset)
					if (element == null || element.text.isBlank()) {
						startOffset = 0
						endOffset = 0
					} else {
						val elementRange = element.textRange
						startOffset = elementRange.startOffset
						endOffset = elementRange.endOffset
					}
				} else  /* isWordTarget */ {
					val wordBoundaries = IntArray(2)
					getWordAtOffset(document, offset, wordBoundaries)
					startOffset = wordBoundaries[0]
					endOffset = wordBoundaries[1]
				}
				if (previousHighlighter != null) markupModel.removeHighlighter(previousHighlighter)
				editor.putUserData(previousHighlighterKey, markupModel.addRangeHighlighter(
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
	
	/**
	 * returns the [PsiElement] at the given offset in the given [Document]
	 *
	 * @param project  instance of [Project]
	 * @param document instance of [Document]
	 * @param offset   offset in the document to find element at
	 * @return the element
	 */
	fun getElementAtOffset(
		project: Project,
		document: Document,
		offset: Int
	): PsiElement? {
		val file = FileDocumentManager.getInstance().getFile(document)
		return PsiManager.getInstance(project).findFile(file!!)!!.findElementAt(offset)
	}
	
	/**
	 * returns the [PsiElement] located at caret
	 *
	 * @param project  instance of current [Project]
	 * @param document instance of the [Document]
	 * @param caret    the [Caret] to find the element at
	 * @return the element located at the specified [Caret]
	 */
	@JvmStatic
	fun getElementAtCaret(
		project: Project,
		document: Document,
		caret: Caret
	): PsiElement? = getElementAtOffset(project, document, caret.offset)
	
	/**
	 * returns the word at the specified offset in the given document
	 *
	 * @param document instance of [Document]
	 * @param offset   offset in the document to find word at
	 * @param wb       (optional) an int[2] to revive word boundaries
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
		val cutil = CaretMovementUtil(caret)
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
