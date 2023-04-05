package ir.mmd.intellijDev.Actionable.lang.java.type

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.lang.java.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.prevLeafNoWhitespace
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction

class JITRefactoringInsert : TypedHandlerDelegate() {
	override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile) = Result.CONTINUE.also {
		if (
			!service<SettingsState>().jitRefactoringEnabled ||
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
			document.getText(TextRange(startOffset, endOffset + 1))
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

class JITRefactoringDelete : BackspaceHandlerDelegate() {
	override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {}
	override fun charDeleted(c: Char, file: PsiFile, editor: Editor) = false after {
		if (
			!service<SettingsState>().jitRefactoringEnabled ||
			file.fileType !is JavaFileType
		) return@after
		
		val document = editor.document
		val offset = editor.caretModel.primaryCaret.offset
		val element = file.findElementAt(offset) ?: return@after
		val localVariable = element.parentOfType<PsiLocalVariable>() ?: return@after
		val newIdentifier = element.textRange.run {
			document.getText(TextRange(startOffset, endOffset - 1))
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
