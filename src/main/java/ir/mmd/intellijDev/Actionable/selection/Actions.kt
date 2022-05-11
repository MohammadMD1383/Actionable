package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteral
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withService

class SelectWordUnderCaretAction : SelectTextUnderCaretAction() {
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? = withService<SettingsState, IntRange?> {
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

class SelectLiteralElementUnderCaretAction : SelectTextUnderCaretAction() {
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? {
		val (start, end) = psiFile.elementAt(caret)?.parentOfType<PsiLiteral>(true)?.textRange ?: return null
		return start..end
	}
}
