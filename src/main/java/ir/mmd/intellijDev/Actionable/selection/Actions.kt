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
		val startCandidateOffset = offset - 1
		val backwardDistance = (caret.offset - startCandidateOffset).absoluteValue
		val startCandidateChar = document[startCandidateOffset]
		reset()
		
		if (!moveUntilReached("\"'`", "\n", FORWARD)) return@run null
		val endCandidateOffset = offset + 1
		val forwardDistance = (caret.offset - endCandidateOffset).absoluteValue
		val endCandidateChar = document[endCandidateOffset]
		reset()
		
		if (startCandidateChar == endCandidateChar)
			return@run startCandidateOffset..endCandidateOffset
		
		if (backwardDistance < forwardDistance) {
			if (!moveUntilReached(startCandidateChar.toString(), "\n", FORWARD)) return@run null
			return@run startCandidateOffset..offset + 1
		}
		
		if (backwardDistance > forwardDistance) {
			if (!moveUntilReached(endCandidateChar.toString(), "\n", BACKWARD)) return@run null
			return@run offset - 1..endCandidateOffset
		}
		
		return@run null
	}
	
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? {
		val element = psiFile.elementAt(caret)
		
		return when (psiFile.fileType.name.lowercase()) {
			"java" -> (element as? PsiLiteralExpression)?.textRange?.intRange
			
			"kotlin" -> (element as? KtStringTemplateExpression)?.textRange?.intRange
			
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
