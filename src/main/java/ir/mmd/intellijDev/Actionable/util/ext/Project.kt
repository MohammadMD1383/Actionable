package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager

inline fun Project.runWriteCommandAction(noinline action: () -> Unit) = WriteCommandAction.runWriteCommandAction(this, action)
inline fun <T> Project.runWriteCommandActionWith(obj: T, crossinline action: (T) -> Unit) = runWriteCommandAction { action(obj) }
inline fun Project.psiFileFor(document: Document) = PsiDocumentManager.getInstance(this).getPsiFile(document)
inline fun <reified T> Project.service(): T = ServiceManager.getService(this, T::class.java)
inline fun <reified T, R> Project.withService(block: T.() -> R) = ServiceManager.getService(this, T::class.java).block()
