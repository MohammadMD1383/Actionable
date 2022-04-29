package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

inline fun Project.runWriteCommandAction(noinline action: () -> Unit) = WriteCommandAction.runWriteCommandAction(this, action)
inline fun Project.psiFileFor(document: Document) = PsiDocumentManager.getInstance(this).getPsiFile(document)

inline val AnActionEvent.editor: Editor? get() = getData(CommonDataKeys.EDITOR)
inline val AnActionEvent.psiFile: PsiFile? get() = getData(CommonDataKeys.PSI_FILE)
val AnActionEvent.primaryCaret: Caret? get() = editor?.caretModel?.primaryCaret

inline fun Document.charAtOrNull(offset: Int) = charsSequence.getOrNull(offset)

inline val Caret.selectionRange: IntRange get() = selectionStart..selectionEnd
inline val Caret.util: CaretUtil get() = CaretUtil(this)

inline fun PsiFile.elementAt(caret: Caret) = findElementAt(caret.offset)

fun String.copyToClipboard() = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(this), null)

inline fun <T> T.runOnly(block: T.() -> Unit) = block()
inline fun <T> T.letOnly(block: (T) -> Unit) = block(this)

fun String.isAllDistinct() = toCharArray().distinct().size == length

inline operator fun String.contains(char: Char?): Boolean = char != null && indexOf(char) >= 0

inline val Int.isPositive: Boolean get() = this > 0
