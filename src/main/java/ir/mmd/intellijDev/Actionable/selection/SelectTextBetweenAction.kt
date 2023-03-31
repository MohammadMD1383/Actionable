package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.util.ext.util

abstract class SelectTextBetweenAction(private val char: String) : SelectTextUnderCaretAction() {
	context (LazyEventContext)
	override fun getSelectionRange(caret: Caret): IntRange? {
		val cutil = caret.util
		
		if (!cutil.moveUntilReached(char, "\n", BACKWARD)) return null
		val start = cutil.offset
		cutil.reset()
		
		if (!cutil.moveUntilReached(char, "\n", FORWARD)) return null
		val end = cutil.offset
		
		return start..end
	}
}

class SelectTextBetweenQuotesAction : SelectTextBetweenAction("'")
class SelectTextBetweenDoubleQuotesAction : SelectTextBetweenAction("\"")
class SelectTextBetweenBackticksAction : SelectTextBetweenAction("`")
