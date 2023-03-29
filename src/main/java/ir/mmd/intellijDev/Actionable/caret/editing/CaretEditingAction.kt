package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseMotionListener
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.util.Key
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.SingleCaretAction
import ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.afterDoing
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState

abstract class CaretEditingAction : SingleCaretAction() {
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
						previousHighlighterKey, addRangeHighlighter(
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
	
	fun Editor.removeScheduledPasteAction() = service<SettingsState>().run {
		if (showPasteActionHints) {
			removeEditorMouseMotionListener(motionListener)
			getUserData(previousHighlighterKey)?.let { markupModel.removeHighlighter(it) }
			putUserData(previousHighlighterKey, null)
		}
		
		putUserData(scheduledPasteActionKind, null)
		putUserData(scheduledPasteActionOffset, null)
	}
	
	context (LazyEventContext)
	fun copyElementAtCaret(deleteElement: Boolean) = psiFile.elementAt(primaryCaret)!!.run {
		text.copyToClipboard()
		if (deleteElement) project.runWriteCommandAction(::delete)
	}
	
	context (LazyEventContext)
	fun copyWordAtCaret(deleteWord: Boolean) {
		val boundaries = IntArray(2)
		val word = primaryCaret.util.getAssociatedWord(boundaries) ?: return
		
		word.copyToClipboard()
		if (deleteWord) {
			project.runWriteCommandAction {
				document.deleteString(boundaries[0], boundaries[1])
			}
		}
	}
	
	context (LazyEventContext)
	fun setPasteOffset(actionName: String) = with(editor) {
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
	
	context(LazyEventContext)
	override val actionEnabled: Boolean
		get() = hasProject
	
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
