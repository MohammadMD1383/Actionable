package ir.mmd.intellijDev.Actionable.typing.java

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.PsiModifier.ABSTRACT
import com.intellij.psi.PsiModifier.DEFAULT
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.typing.java.state.State
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.*

@Keep
class AutoInsertSemicolon : TypedHandlerDelegate() {
	override fun beforeCharTyped(
		c: Char,
		project: Project,
		editor: Editor,
		file: PsiFile,
		fileType: FileType
	) = Result.CONTINUE after {
		if (
			!project.service<State>().autoInsertSemicolonEnabled ||
			file.fileType !is JavaFileType ||
			c != '('
		) return@after
		
		val caret = editor.caretModel.primaryCaret
		val element = file.elementAt(caret)?.prevLeaf(true)?.parentOfType<PsiField>(true) ?: return@after
		val clazz = element.containingClass!!
		
		if (
			(clazz.isInterface and !element.hasModifierProperty(DEFAULT)) or
			element.hasModifierProperty(ABSTRACT)
		) return@after
		
		if (caret.util.nextChar == ';') project.runWriteCommandAction {
			editor.document.removeCharAt(caret.offset)
		}
	}
	
	override fun charTyped(
		c: Char,
		project: Project,
		editor: Editor,
		file: PsiFile
	) = Result.CONTINUE.also {
		if (
			!project.service<State>().autoInsertSemicolonEnabled ||
			file.fileType !is JavaFileType ||
			c == ';'
		) return@also
		
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		val element = file.elementAt(caret)
			?.prevLeafNoWhitespace(true)
			?.parentOfTypes(
				PsiField::class,
				PsiLocalVariable::class,
				PsiAssignmentExpression::class,
				PsiReturnStatement::class,
				withSelf = true
			) ?: return@also
		
		if (
			element is PsiEnumConstant ||
			element.textContains(';') ||
			element.nextLeafNoWhitespace(true).let { it is PsiJavaToken && it.tokenType == JavaTokenType.SEMICOLON }
		) return@also
		
		val lineEnd = document.getLineEndOffset(caret.logicalPosition.line)
		project.runWriteCommandAction {
			document.insertString(lineEnd, ";")
		}
	}
}
