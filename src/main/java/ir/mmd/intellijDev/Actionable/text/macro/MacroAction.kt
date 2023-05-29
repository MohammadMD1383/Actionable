package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.project.DumbAware
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretAction
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTranspiler
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.*
import kotlin.math.max
import kotlin.math.min

val macroPlaceholderNames = listOf("SELECTION", "WORD", "ELEMENT", "LINE", "NUMBER")

class MacroAction(name: String, private val macro: String) : MultiCaretAction(name), DumbAware {
	context (LazyEventContext)
	override fun perform() {
		var replacementStart = caret.selectionStart
		var replacementEnd = caret.selectionEnd
		val offset: Int
		
		val text = MacroTemplateTranspiler(project, macro) {
			when (it) {
				"SELECTION" -> {
					replacementStart = min(replacementStart, selectionModel.selectionStart)
					replacementEnd = max(replacementEnd, selectionModel.selectionEnd)
					selectionModel.selectedText ?: ""
				}
				
				"ELEMENT" -> {
					psiFile.elementAtOrBefore(caret)?.also { el ->
						replacementStart = min(replacementStart, el.startOffset)
						replacementEnd = max(replacementEnd, el.endOffset)
					}?.text ?: ""
				}
				
				"WORD" -> {
					val boundaries = IntArray(2)
					(document.getWordAtOffset(caret.offset, boundaries) ?: "") after {
						replacementStart = min(replacementStart, boundaries[0])
						replacementEnd = max(replacementEnd, boundaries[1])
					}
				}
				
				"LINE" -> {
					val boundaries = IntArray(2)
					document.getLineText(caret, boundaries) after {
						replacementStart = min(replacementStart, boundaries[0])
						replacementEnd = max(replacementEnd, boundaries[1])
					}
				}
				
				"NUMBER" -> {
					val boundaries = IntArray(2)
					(document.getNumberAt(caret, boundaries) ?: "") after {
						replacementStart = min(replacementStart, boundaries[0])
						replacementEnd = max(replacementEnd, boundaries[1])
					}
				}
				
				else -> ""
			}
		}.run {
			compute()
			offset = finalCaretOffset
			getText()
		}
		
		runWriteCommandAction {
			document.replaceString(replacementStart, replacementEnd, text)
			caret moveTo replacementStart + offset
		}
	}
	
	context(LazyEventContext)
	override fun isEnabled() = hasEditor
}
