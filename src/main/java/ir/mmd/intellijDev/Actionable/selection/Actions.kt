package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.ext.component1
import ir.mmd.intellijDev.Actionable.util.ext.component2
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.util
import ir.mmd.intellijDev.Actionable.util.withMovementSettings

class SelectWordUnderCaretAction : SelectTextUnderCaretAction() {
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? = withMovementSettings {
		val (start, end) = caret.util.getWordBoundaries(wordSeparators, hardStopCharacters)
		return if (start != end) start..end else null
	}
}

class SelectElementUnderCaretAction : SelectTextUnderCaretAction() {
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? {
		val (start, end) = psiFile.elementAt(caret)?.textRange ?: return null
		return start..end
	}
}
