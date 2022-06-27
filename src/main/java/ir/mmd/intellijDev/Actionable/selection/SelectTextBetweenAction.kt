package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.ext.util

abstract class SelectTextBetweenAction(private val char: String) : SelectTextUnderCaretAction() {
	override fun getSelectionRange(caret: Caret, psiFile: PsiFile): IntRange? {
		val cutil = caret.util
		
		if (!cutil.moveUntilReached(char, "\n", FORWARD)) return null
		val start = cutil.offset
		
		if (!cutil.moveUntilReached(char, "\n", BACKWARD)) return null
		val end = cutil.offset
		
		return start..end
	}
}
