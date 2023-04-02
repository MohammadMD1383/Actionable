package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.components.service
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
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.SingleCaretAction
import ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.afterDoing
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

abstract class TargetedCopyPasteAction : SingleCaretAction() {
	companion object {
		@JvmStatic
		protected val scheduledPasteActionKind = Key<String>("scheduledPasteAction.kink")
		
		@JvmStatic
		protected val scheduledPasteActionOffset = Key<Int>("scheduledPasteAction.offset")
		
		@JvmStatic
		protected val previousHighlighterKey = Key<RangeHighlighter>("scheduledPasteAction.motionListener")
		
		private val motionListener = object : EditorMouseMotionListener {
			override fun mouseMoved(e: EditorMouseEvent): Unit = service<MovementSettingsState>().run {
				val editor = e.editor
				val previousHighlighter = editor.getUserData(previousHighlighterKey)
				val offset = editor.run { logicalPositionToOffset(xyToLogicalPosition(e.mouseEvent.point)) }
				if (offset in previousHighlighter?.highlightRange) return
				
				val startOffset: Int
				val endOffset: Int
				
				when ((editor.getUserData(scheduledPasteActionKind) ?: return).split(';')[0]) {
					"el" -> editor.project!!.psiFileFor(editor.document)!!.findElementAt(offset).run {
						if (this == null || text.isBlank()) {
							startOffset = 0
							endOffset = 0
						} else textRange.let { (start, end) ->
							startOffset = start
							endOffset = end
						}
					}
					
					"wd" -> editor.document.getWordBoundaries(offset, wordSeparators + hardStopCharacters).let { (start, end) ->
						startOffset = start
						endOffset = end
					}
					
					else -> throw Exception("Unknown scheduled paste action target")
				}
				
				editor.markupModel.run {
					previousHighlighter?.let { removeHighlighter(it) }
					editor.putUserData(
						previousHighlighterKey,
						addRangeHighlighter(
							startOffset,
							endOffset,
							HighlighterLayer.LAST + 10,
							EditorColors.IDENTIFIER_UNDER_CARET_ATTRIBUTES.defaultAttributes,
							HighlighterTargetArea.EXACT_RANGE
						)
					)
				}
			}
			
			override fun mouseDragged(e: EditorMouseEvent) {}
		}
	}
	
	protected fun Editor.removeScheduledPasteAction() = service<SettingsState>().run {
		if (showPasteActionHints) {
			removeEditorMouseMotionListener(motionListener)
			getUserData(previousHighlighterKey)?.let { markupModel.removeHighlighter(it) }
			putUserData(previousHighlighterKey, null)
		}
		
		putUserData(scheduledPasteActionKind, null)
		putUserData(scheduledPasteActionOffset, null)
	}
	
	context (LazyEventContext)
	protected fun setPasteOffset(actionName: String) = with(editor) {
		if (actionName == getUserData(scheduledPasteActionKind)) return afterDoing {
			if (primaryCaret.offset == getUserData(scheduledPasteActionOffset)) {
				removeScheduledPasteAction()
			} else {
				putUserData(scheduledPasteActionOffset, primaryCaret.offset)
			}
		}
		
		putUserData(scheduledPasteActionKind, actionName)
		putUserData(scheduledPasteActionOffset, primaryCaret.offset)
		if (service<SettingsState>().showPasteActionHints) addEditorMouseMotionListener(motionListener)
	}
}

class SetWordCutPasteOffset : TargetedCopyPasteAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("wd;ct")
}

class SetWordCopyPasteOffset : TargetedCopyPasteAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("wd;cp")
}

class SetElementCutPasteOffset : TargetedCopyPasteAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("el;ct")
}

class SetElementCopyPasteOffset : TargetedCopyPasteAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("el;cp")
}

class CancelPasteAction : TargetedCopyPasteAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = editor.removeScheduledPasteAction()
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && editor.getUserData(scheduledPasteActionKind) != null
}

class ExecutePasteAction : TargetedCopyPasteAction() {
	context (LazyEventContext)
	override fun perform(caret: Caret) = service<ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState>().run {
		val (target, action) = (editor.getUserData(scheduledPasteActionKind) ?: return).split(';')
		
		val startOffset: Int
		val endOffset: Int
		
		when (target) {
			"el" -> psiFile.elementAt(caret)!!.textRange.let { (start, end) ->
				startOffset = start
				endOffset = end
			}
			
			"wd" -> caret.util.getWordBoundaries(wordSeparators, hardStopCharacters).let { (start, end) ->
				startOffset = start
				endOffset = end
			}
			
			else -> throw Exception("Unknown scheduled paste action target")
		}
		
		runWriteCommandAction {
			paste(
				document,
				caret,
				startOffset,
				endOffset,
				editor.getUserData(scheduledPasteActionOffset)!!,
				action == "ct"
			)
		}
		editor.removeScheduledPasteAction()
	}
	
	/**
	 * will paste the specified range of text into the specified offset
	 * and will take care of *cut* action
	 *
	 * @param document instance of the [Document]
	 * @param caret    the caret to move to the final offset after the paste operation
	 * @param start    text start position
	 * @param end      text end position
	 * @param offset   offset to paste at
	 * @param isCutAction    whether to delete the text after pasting or not
	 */
	private fun paste(
		document: Document,
		caret: Caret,
		start: Int,
		end: Int,
		offset: Int,
		isCutAction: Boolean
	) {
		if (start == end) return
		val text = document.getText(TextRange(start, end))
		
		if (offset > start && offset > end) {
			document.insertString(offset, text)
			caret.moveToOffset(offset + text.length)
			if (isCutAction) document.deleteString(start, end)
		} else if (offset < start && offset < end) {
			if (isCutAction) document.deleteString(start, end)
			document.insertString(offset, text)
			caret.moveToOffset(offset + text.length)
		} /* else -> offset is between the start and end -> ignore */
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && caretCount == 1 && editor.getUserData(scheduledPasteActionKind) != null
}
