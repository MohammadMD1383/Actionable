package ir.mmd.intellijDev.Actionable.selection

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.ecma6.JSStringTemplateExpression
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withService
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import kotlin.math.absoluteValue

@Keep
class SelectWordUnderCaretAction : SelectTextUnderCaretAction() {
	override fun isDumbAware() = true
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile) = withService<SettingsState, IntRange?> {
		val (start, end) = caret.util.getWordBoundaries(wordSeparators, hardStopCharacters)
		return if (start != end) start..end else null
	}
}

@Keep
class SelectElementUnderCaretAction : SelectTextUnderCaretAction() {
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? {
		val (start, end) = psiFile.elementAt(caret)?.textRange ?: return null
		return start..end
	}
}

@Keep
class SelectLiteralElementUnderCaretAction : SelectTextUnderCaretAction() {
	private fun rawSelectionRange(caret: Caret) = caret.util.run {
		if (!moveUntilReached("\"'`", "\n", BACKWARD)) return@run null
		val startCandidateOffset = prevCharOffset
		val startCandidateChar = prevChar!!
		val backwardDistance = (caret.offset - startCandidateOffset).absoluteValue
		reset()
		
		if (!moveUntilReached("\"'`", "\n", FORWARD)) return@run null
		val endCandidateOffset = nextCharOffset
		val endCandidateChar = nextChar!!
		val forwardDistance = (caret.offset - endCandidateOffset).absoluteValue
		reset()
		
		if (startCandidateChar == endCandidateChar)
			return@run startCandidateOffset..endCandidateOffset + 1
		
		if (backwardDistance < forwardDistance) {
			if (!moveUntilReached(startCandidateChar.toString(), "\n", FORWARD)) return@run null
			return@run startCandidateOffset..nextCharOffset + 1
		}
		
		if (backwardDistance > forwardDistance) {
			if (!moveUntilReached(endCandidateChar.toString(), "\n", BACKWARD)) return@run null
			return@run prevCharOffset..endCandidateOffset + 1
		}
		
		return@run null
	}
	
	private fun shrinkSelection(caret: Caret): IntRange? {
		val text = caret.selectedText!!
		val first = text.first()
		val last = text.last()
		
		if (
			first == '"' && last == '"' ||
			first == '\'' && last == '\'' ||
			first == '`' && last == '`'
		) {
			val oldStart = caret.selectionStart
			val start = text.indexOfFirst { it != first }
			val end = text.indexOfLast { it != last }
			
			return (oldStart + start)..(oldStart + end + 1)
		}
		
		return null
	}
	
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? {
		if (caret.hasSelection())
			shrinkSelection(caret)?.let { return it }
		
		val element = psiFile.elementAt(caret)
		
		return when (psiFile.fileType.name.lowercase()) {
			"java" -> (element as? PsiLiteralExpression)?.textRange?.intRange
			
			"kotlin" -> element?.parentOfType<KtStringTemplateExpression>(true)?.textRange?.intRange
			
			"javascript" -> element?.parentOfTypes(
				JSStringTemplateExpression::class,
				JSLiteralExpression::class,
				withSelf = true
			)?.textRange?.intRange
			
			else -> rawSelectionRange(caret)
		}
	}
}

@Keep
class SelectTextBetweenQuotesAction : SelectTextBetweenAction("'")

@Keep
class SelectTextBetweenDoubleQuotesAction : SelectTextBetweenAction("\"")

@Keep
class SelectTextBetweenBackticksAction : SelectTextBetweenAction("`")
