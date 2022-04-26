package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Suppress("NOTHING_TO_INLINE")
inline fun Project.runWriteCommandAction(noinline action: () -> Unit) {
	WriteCommandAction.runWriteCommandAction(this, action)
}

/**
 * copies the given string to system clipboard
 */
fun String.copyToClipboard() {
	Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(this), null)
}
