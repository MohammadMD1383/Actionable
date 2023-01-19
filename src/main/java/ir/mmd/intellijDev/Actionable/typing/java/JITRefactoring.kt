package ir.mmd.intellijDev.Actionable.typing.java

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.search.searches.ReferencesSearch
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.typing.java.state.State
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.falseAfter

@Keep
class JITRefactoringInsert : TypedHandlerDelegate() {
	override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile) = Result.CONTINUE.also {
		if (
			!project.service<State>().jitRefactoringEnabled ||
			file.fileType !is JavaFileType
		) return@also
		
		val document = editor.document
		val offset = editor.caretModel.primaryCaret.offset
		var element = file.findElementAt(offset) ?: return@also
		
		if (element !is PsiIdentifier)
			element = element.prevLeafNoWhitespace(true) ?: return@also
		if (element !is PsiIdentifier)
			return@also
		
		val localVariable = element.parentOfType<PsiLocalVariable>() ?: return@also
		val newIdentifier = element.textRange.run {
			document.getText(startOffset..endOffset + 1)
		}
		
		project.runWriteCommandAction {
			ReferencesSearch.search(localVariable).findAll().forEach {
				it.element.textRange.run {
					document.replaceString(startOffset + 1, endOffset + 1, newIdentifier)
				}
			}
		}
	}
}

@Keep
class JITRefactoringDelete : BackspaceHandlerDelegate() {
	override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {}
	override fun charDeleted(c: Char, file: PsiFile, editor: Editor) = falseAfter {
		if (
			!editor.project!!.service<State>().jitRefactoringEnabled ||
			file.fileType !is JavaFileType
		) return@falseAfter
		
		val document = editor.document
		val offset = editor.caretModel.primaryCaret.offset
		val element = file.findElementAt(offset) ?: return@falseAfter
		val localVariable = element.parentOfType<PsiLocalVariable>() ?: return@falseAfter
		val newIdentifier = element.textRange.run {
			document.getText(startOffset until endOffset)
		}
		
		editor.project.runWriteCommandAction {
			ReferencesSearch.search(localVariable).findAll().forEach {
				it.element.textRange.run {
					document.replaceString(startOffset - 1, endOffset - 1, newIdentifier)
				}
			}
		}
	}
}
