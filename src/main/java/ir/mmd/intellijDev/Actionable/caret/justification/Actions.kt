package ir.mmd.intellijDev.Actionable.caret.justification

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/**
 * this class holds implementation of these actions:
 *
 *  * [JustifyCaretsStart]
 *  * [JustifyCaretsEnd]
 *  * [JustifyCaretsEndAndShift]
 *
 */
object Actions {
	/**
	 * common availability criteria among these actions:
	 *
	 *  * [JustifyCaretsStart]
	 *  * [JustifyCaretsEnd]
	 *  * [JustifyCaretsEndAndShift]
	 *
	 *
	 * @param e event of execution
	 */
	fun setActionAvailability(e: AnActionEvent) {
		val project = e.project
		val editor = e.getData(CommonDataKeys.EDITOR)
		e.presentation.isEnabled = project != null && editor != null && editor.caretModel.caretCount > 1 && !editor.selectionModel.hasSelection(true)
	}
	
	/**
	 * executes the given `justifier`
	 *
	 * @param e         event of execution
	 * @param justifier a method of [JustifyCaretUtil]
	 */
	fun justifyCarets(
		e: AnActionEvent,
		justifier: JustifyCaretUtil.() -> Unit
	) {
		val editor = e.getRequiredData(CommonDataKeys.EDITOR)
		JustifyCaretUtil(editor).justifier()
	}
}
