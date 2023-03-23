package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager

/**
 * Helper method to be used instead of `WriteCommandAction.runWriteCommandAction([Project]) { ... }`
 *
 * @see [WriteCommandAction.runWriteCommandAction]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Project?.runWriteCommandAction(noinline action: () -> Unit) = WriteCommandAction.runWriteCommandAction(this, action)

/**
 * Returns [com.intellij.psi.PsiFile] for the specified [document]
 *
 * @receiver the active project - usually [com.intellij.openapi.actionSystem.AnActionEvent.getProject]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Project.psiFileFor(document: Document) = PsiDocumentManager.getInstance(this).getPsiFile(document)

/**
 * Returns a project-wide service
 */
inline fun <reified T> Project.service(): T = getService(T::class.java)

/**
 * Returns a project-wide service
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Project.service(clazz: Class<T>): T = getService(clazz)
