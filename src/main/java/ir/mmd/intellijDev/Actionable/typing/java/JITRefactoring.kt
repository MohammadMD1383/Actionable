package ir.mmd.intellijDev.Actionable.typing.java

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiReferenceService
import com.intellij.psi.PsiReferenceService.Hints.NO_HINTS
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.containers.toArray
import ir.mmd.intellijDev.Actionable.typing.java.state.State
import ir.mmd.intellijDev.Actionable.util.ext.prevLeafNoWhitespace
import ir.mmd.intellijDev.Actionable.util.ext.replaceString
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction
import ir.mmd.intellijDev.Actionable.util.ext.service
import ir.mmd.intellijDev.Actionable.util.trueAfter

class JITRefactoringInsert : TypedHandlerDelegate() {
	override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile) = Result.CONTINUE.also {
		if (
			!project.service<State>().jitRefactoringEnabled ||
			file.fileType !is JavaFileType
		) return@also
		
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		val offset = caret.offset
		var element = file.findElementAt(offset) ?: return@also
		
		if (element !is PsiIdentifier)
			element = element.prevLeafNoWhitespace(true) ?: return@also
		if (element !is PsiIdentifier)
			return@also
		
		val var1 = element.reference
		val var2 = element.references
		val var3 = ReferencesSearch.search(element).findAll()
		
		val newText = element.text
		project.runWriteCommandAction {
			PsiReferenceService.getService().getReferences(element, NO_HINTS).forEach {
				document.replaceString(it.element.textRange, newText)
			}
		}
	}
}

class JITRefactoringDelete : BackspaceHandlerDelegate() {
	override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {}
	override fun charDeleted(c: Char, file: PsiFile, editor: Editor) = trueAfter {
		if (
			!editor.project!!.service<State>().jitRefactoringEnabled ||
			file.fileType !is JavaFileType
		) return@trueAfter
		
		
	}
}
