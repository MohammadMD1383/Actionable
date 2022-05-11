package ir.mmd.intellijDev.Actionable.typing.java

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiField
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiModifier.ABSTRACT
import com.intellij.psi.PsiModifier.DEFAULT
import com.intellij.psi.PsiReturnStatement
import ir.mmd.intellijDev.Actionable.typing.java.state.State
import ir.mmd.intellijDev.Actionable.util.ext.*

class AutoInsertSemicolon : TypedHandlerDelegate() {
	override fun beforeCharTyped(
		c: Char,
		project: Project,
		editor: Editor,
		file: PsiFile,
		fileType: FileType
	) = Result.CONTINUE.also {
		if (
			!project.service<State>().autoInsertSemicolonEnabled ||
			file.fileType !is JavaFileType ||
			c != '('
		) return@also
		
		val caret = editor.caretModel.primaryCaret
		val element = file.elementAt(caret)?.prevLeaf(true)?.parentOfType<PsiField>(true) ?: return@also
		val clazz = element.containingClass!!
		
		if (
			(clazz.isInterface and !element.hasModifierProperty(DEFAULT)) or
			element.hasModifierProperty(ABSTRACT)
		) return@also
		
		if (caret.util.nextChar == ';') project.runWriteCommandActionWith(caret.offset) {
			editor.document.deleteString(it, it + 1)
		}
	}
	
	override fun charTyped(
		c: Char,
		project: Project,
		editor: Editor,
		file: PsiFile
	) = Result.CONTINUE.also {
		if (
			file.fileType !is JavaFileType ||
			c == ';'
		) return@also
		
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		val element = file.elementAt(caret)
			?.prevLeafNoWhitespace(true)
			?.parentOfTypes(PsiField::class, PsiLocalVariable::class, PsiReturnStatement::class, withSelf = true)
			?: return@also
		
		if (!element.textContains(';')) {
			val lineEnd = document.getLineEndOffset(caret.logicalPosition.line)
			project.runWriteCommandAction {
				document.insertString(lineEnd, ";")
			}
		}
	}
}
