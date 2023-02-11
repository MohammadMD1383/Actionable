package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

/**
 * Checks whether the [AnActionEvent] has a [PsiFile] instance or not
 *
 * @see [AnActionEvent.hasPsiFileWith]
 */
inline val AnActionEvent.hasPsiFile: Boolean get() = getData(CommonDataKeys.PSI_FILE) != null

/**
 * Checks whether the [AnActionEvent] has a [PsiFile] **and that psiFile satisfies a given criteria**
 *
 * @param block the criteria
 * @see [AnActionEvent.hasPsiFile]
 */
inline fun AnActionEvent.hasPsiFileWith(block: PsiFile.() -> Boolean) = getData(CommonDataKeys.PSI_FILE)?.block() ?: false

/**
 * Checks whether the [AnActionEvent] has an [Editor] instance or not
 *
 * @see [AnActionEvent.hasEditorWith]
 */
inline val AnActionEvent.hasEditor: Boolean get() = getData(CommonDataKeys.EDITOR) != null

/**
 * Checks whether the [AnActionEvent] has an [Editor] **and that editor satisfies a given criteria**
 *
 * @param block the criteria
 * @see [AnActionEvent.hasEditor]
 */
inline fun AnActionEvent.hasEditorWith(block: Editor.() -> Boolean) = getData(CommonDataKeys.EDITOR)?.block() ?: false

/**
 * Checks whether the [AnActionEvent] has a [com.intellij.openapi.project.Project] instance
 */
inline val AnActionEvent.hasProject: Boolean get() = project != null

/**
 * Returns an [Editor]
 *
 * @see [AnActionEvent.hasEditor]
 */
inline val AnActionEvent.editor: Editor get() = getRequiredData(CommonDataKeys.EDITOR)

/**
 * Returns a [PsiFile]
 */
inline val AnActionEvent.psiFile: PsiFile get() = getRequiredData(CommonDataKeys.PSI_FILE)

/**
 * Returns primary [Caret] of the current [Editor]
 *
 * @see [AnActionEvent.editor]
 * @see [Editor.getCaretModel]
 * @see [com.intellij.openapi.editor.CaretModel.getPrimaryCaret]
 */
inline val AnActionEvent.primaryCaret: Caret get() = editor.caretModel.primaryCaret

/**
 * Returns a [List] of all [Caret]s in the current [Editor]
 *
 * @see [AnActionEvent.editor]
 * @see [AnActionEvent.primaryCaret]
 * @see [Editor.getCaretModel]
 * @see [com.intellij.openapi.editor.CaretModel.getAllCarets]
 */
inline val AnActionEvent.allCarets: MutableList<Caret> get() = editor.caretModel.allCarets

/**
 * Sets the presentation of the current action to the result of [block]
 *
 * Equivalent of [AnActionEvent.getPresentation] and [com.intellij.openapi.actionSystem.Presentation.setEnabled] using result of the [block]
 *
 * @param block the criteria
 */
inline fun AnActionEvent.enableIf(block: AnActionEvent.() -> Boolean) {
	presentation.isEnabled = block()
}
