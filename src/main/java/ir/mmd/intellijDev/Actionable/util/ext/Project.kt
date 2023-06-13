package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager

/**
 * Helper method to be used instead of `WriteCommandAction.runWriteCommandAction([Project]) { ... }`
 *
 * @see WriteCommandAction.runWriteCommandAction
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
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Project.service(clazz: Class<T>): T = getService(clazz)

/**
 * create a command that can be used with [com.intellij.navigation.JBProtocolNavigateCommand]
 */
fun Project.createNavigationCommand(
	pathOrFqn: String, line: Int? = null, column: Int? = null,
	selectionStartLine: Int? = null, selectionStartColumn: Int? = null,
	selectionEndLine: Int? = null, selectionEndColumn: Int? = null,
	fqn: Boolean = false
): String {
	val pof = if (fqn) "fqn" else "path"
	
	var position = ""
	line?.let {
		position = ":$line"
		column?.let {
			position += ":$column"
		}
	}
	
	var selection = ""
	if (selectionStartLine != null && selectionStartColumn != null && selectionEndLine != null && selectionEndColumn != null) {
		selection = "&selection=$selectionStartLine:$selectionStartColumn-$selectionEndLine:$selectionEndColumn"
	}
	
	return "jetbrains:/navigate/reference?project=${name.urlEncode()}&$pof=${pathOrFqn.urlEncode()}$position$selection"
}
