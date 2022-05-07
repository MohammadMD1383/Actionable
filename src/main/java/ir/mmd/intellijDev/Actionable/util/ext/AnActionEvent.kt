package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

inline val AnActionEvent.hasEditor: Boolean get() = getData(CommonDataKeys.EDITOR) != null
inline fun AnActionEvent.hasEditorWith(block: Editor.() -> Boolean) = getData(CommonDataKeys.EDITOR)?.block() ?: false
inline val AnActionEvent.hasProject: Boolean get() = project != null
inline val AnActionEvent.editor: Editor get() = getRequiredData(CommonDataKeys.EDITOR)
inline val AnActionEvent.psiFile: PsiFile get() = getRequiredData(CommonDataKeys.PSI_FILE)
inline val AnActionEvent.primaryCaret: Caret get() = editor.caretModel.primaryCaret
inline val AnActionEvent.allCarets: MutableList<Caret> get() = editor.caretModel.allCarets

inline fun AnActionEvent.enableIf(block: AnActionEvent.() -> Boolean) {
	presentation.isEnabled = block()
}
