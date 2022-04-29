package ir.mmd.intellijDev.Actionable.caret.justification

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.editor

abstract class JustifyAction(private val justifier: JustifyCaretUtil.() -> Unit) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = JustifyCaretUtil(e.editor!!).justifier()
	override fun update(e: AnActionEvent) {
		e.presentation.isEnabled = e.project != null && e.editor?.run {
			caretModel.caretCount > 1 && selectionModel.hasSelection(true).not()
		} == true
	}
}

class JustifyCaretsEndAndShift : JustifyAction(JustifyCaretUtil::justifyCaretsEndWithShifting)
class JustifyCaretsStart : JustifyAction(JustifyCaretUtil::justifyCaretsStart)
class JustifyCaretsEnd : JustifyAction(JustifyCaretUtil::justifyCaretsEnd)
