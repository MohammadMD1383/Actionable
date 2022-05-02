package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseMotionListener
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.util.Key
import ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.by
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withEditingSettings
import ir.mmd.intellijDev.Actionable.util.withMovementSettings

abstract class EditingAction : AnAction() {
	companion object {
		@JvmStatic
		protected val scheduledPasteActionKind = Key<String>("scheduledPasteAction.kink")
		
		@JvmStatic
		protected val scheduledPasteActionOffset = Key<Int>("scheduledPasteAction.offset")
		
		@JvmStatic
		protected val previousHighlighterKey = Key<RangeHighlighter>("scheduledPasteAction.motionListener")
		
		private val motionListener = object : EditorMouseMotionListener {
			override fun mouseMoved(e: EditorMouseEvent) = withMovementSettings {
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
			
			override fun mouseDragged(e: EditorMouseEvent) {}
		}
	}
	
	fun Editor.removeScheduledPasteAction() = withEditingSettings {
		if (showPasteActionHints) {
			removeEditorMouseMotionListener(motionListener)
			getUserData(previousHighlighterKey)?.let { markupModel.removeHighlighter(it) }
			putUserData(previousHighlighterKey, null)
		}
		
		putUserData(scheduledPasteActionKind, null)
		putUserData(scheduledPasteActionOffset, null)
	}
	
	fun copyElementAtCaret(
		e: AnActionEvent,
		deleteElement: Boolean
	) = e.psiFile!!.elementAt(e.primaryCaret)!!.runOnly {
		text.copyToClipboard()
		if (deleteElement) e.project!!.runWriteCommandAction(::delete)
	}
	
	fun copyWordAtCaret(
		e: AnActionEvent,
		deleteWord: Boolean
	) = by(IntArray(2)) {
		e.primaryCaret.util.getAssociatedWord(it)?.run {
			copyToClipboard()
			if (deleteWord) e.project!!.runWriteCommandAction { e.editor.document.deleteString(it[0], it[1]) }
		}
	}
	
	fun setPasteOffset(
		e: AnActionEvent,
		actionName: String
	) = with(e.editor) {
		val caret = caretModel.primaryCaret
		
		if (actionName == getUserData(scheduledPasteActionKind)) return after {
			if (caret.offset == getUserData(scheduledPasteActionOffset))
				removeScheduledPasteAction()
			else
				putUserData(scheduledPasteActionOffset, caret.offset)
		}
		
		putUserData(scheduledPasteActionKind, actionName)
		putUserData(scheduledPasteActionOffset, caret.offset)
		if (SettingsState.getInstance().showPasteActionHints) addEditorMouseMotionListener(motionListener)
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { caretCount == 1 } }
}
