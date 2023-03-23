package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.action.LazyEventContext

/**
 * Returns an [Editor]
 */
inline val AnActionEvent.editor: Editor? get() = getData(CommonDataKeys.EDITOR)

/**
 * Returns a [PsiFile]
 */
inline val AnActionEvent.psiFile: PsiFile? get() = getData(CommonDataKeys.PSI_FILE)

/**
 * Sets the presentation of the current action to the result of [block]
 *
 * if [block] throws an exception then it will be evaluated to false
 *
 * Equivalent of [AnActionEvent.getPresentation] and [com.intellij.openapi.actionSystem.Presentation.setEnabled] using result of the [block]
 *
 * @param block the criteria
 */
inline fun AnActionEvent.enableIf(block: LazyEventContext.() -> Boolean) {
	presentation.isEnabled = try {
		LazyEventContext(this).block()
	} catch (ignored: Exception) {
		false
	}
}
