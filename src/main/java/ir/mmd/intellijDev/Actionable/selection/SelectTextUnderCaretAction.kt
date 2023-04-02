package ir.mmd.intellijDev.Actionable.selection

import com.goide.psi.GoStringLiteral
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.ecma6.JSStringTemplateExpression
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentOfTypes
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.caret.ActionAtCaret
import ir.mmd.intellijDev.Actionable.util.CaretUtil
import ir.mmd.intellijDev.Actionable.util.ext.*
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import kotlin.math.absoluteValue

abstract class SelectTextUnderCaretAction : ActionAtCaret<ActionAtCaret.Model, ActionAtCaret.Model>(removeCarets = false) {
	context (LazyEventContext)
	abstract fun getSelectionRange(caret: Caret): IntRange?
	
	context(LazyEventContext)
	override fun createModel(caret: Caret) = Model(caret)
	
	override fun distinctKey(model: Model) = model
	
	context (LazyEventContext)
	override fun doAction(model: Model) {
		val range = getSelectionRange(model.caret) ?: return
		model.caret.setSelection(range.first, range.last)
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class SelectLineWithoutIndentAction : SelectTextUnderCaretAction(), DumbAware {
	
	context (LazyEventContext)
	override fun getSelectionRange(caret: Caret): IntRange? {
		val lineNumber = document.getLineNumber(caret.offset)
		val start = document.getLineStartOffset(lineNumber) + document.getLineStartIndentLength(lineNumber)
		val end = document.getLineEndOffset(lineNumber) - document.getLineTrailingWhitespaceLength(lineNumber)
		
		return if (start != end) start..end else null
	}
}

class SelectWordUnderCaretAction : SelectTextUnderCaretAction(), DumbAware {
	
	context (LazyEventContext)
	override fun getSelectionRange(caret: Caret): IntRange? {
		val (start, end) = caret.util.getWordBoundaries()
		return if (start != end) start..end else null
	}
}

class SelectElementUnderCaretAction : SelectTextUnderCaretAction() {
	context (LazyEventContext)
	override fun getSelectionRange(caret: Caret): IntRange? {
		val (start, end) = psiFile.elementAt(caret)?.textRange ?: return null
		return start..end
	}
}

class SelectLiteralElementUnderCaretAction : SelectTextUnderCaretAction() {
	private fun rawSelectionRange(caret: Caret) = caret.util.run {
		if (!moveUntilReached("\"'`", "\n", CaretUtil.BACKWARD)) return@run null
		val startCandidateOffset = prevCharOffset
		val startCandidateChar = prevChar!!
		val backwardDistance = (caret.offset - startCandidateOffset).absoluteValue
		reset()
		
		if (!moveUntilReached("\"'`", "\n", CaretUtil.FORWARD)) return@run null
		val endCandidateOffset = nextCharOffset
		val endCandidateChar = nextChar!!
		val forwardDistance = (caret.offset - endCandidateOffset).absoluteValue
		reset()
		
		if (startCandidateChar == endCandidateChar)
			return@run startCandidateOffset..endCandidateOffset + 1
		
		if (backwardDistance < forwardDistance) {
			if (!moveUntilReached(startCandidateChar.toString(), "\n", CaretUtil.FORWARD)) return@run null
			return@run startCandidateOffset..nextCharOffset + 1
		}
		
		if (backwardDistance > forwardDistance) {
			if (!moveUntilReached(endCandidateChar.toString(), "\n", CaretUtil.BACKWARD)) return@run null
			return@run prevCharOffset..endCandidateOffset + 1
		}
		
		return@run null
	}
	
	private fun shrinkSelection(caret: Caret): IntRange? {
		val text = caret.selectedText!!
		val first = text.first()
		val last = text.last()
		
		if (first == last && first in "`'\"") {
			val oldStart = caret.selectionStart
			val start = text.indexOfFirst { it != first }
			val end = text.indexOfLast { it != last }
			
			return (oldStart + start)..(oldStart + end + 1)
		}
		
		return null
	}
	
	context (LazyEventContext)
	override fun getSelectionRange(caret: Caret): IntRange? {
		if (caret.hasSelection())
			shrinkSelection(caret)?.let { return it }
		
		val element = psiFile.elementAt(caret)
		
		return when (psiFile.fileType.name.lowercase()) {
			"java" -> element?.parentOfType<PsiLiteralExpression>(true)?.textRange?.intRange
			
			"kotlin" -> element?.parentOfType<KtStringTemplateExpression>(true)?.textRange?.intRange
			
			"javascript",
			"typescript" -> element?.parentOfTypes(
				JSStringTemplateExpression::class,
				JSLiteralExpression::class,
				withSelf = true
			)?.textRange?.intRange
			
			"go" -> element?.parentOfType<GoStringLiteral>(true)?.textRange?.intRange
			
			else -> rawSelectionRange(caret)
		}
	}
}
