package ir.mmd.intellijDev.Actionable.typing.java

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.typing.java.state.State
import ir.mmd.intellijDev.Actionable.util.ext.*

@Keep
class AutoClassCase : TypedHandlerDelegate() {
	override fun charTyped(
		c: Char,
		project: Project,
		editor: Editor,
		file: PsiFile
	) = Result.CONTINUE.also {
		if (
			!project.service<State>().autoClassCaseEnabled ||
			file.fileType !is JavaFileType ||
			c != '{'
		) return@also
		
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		val element = file.elementAtOrBefore(caret)
			?.prevLeafNoWhitespace(true)
			?.parentOfType<PsiClass>(true)
			?: return@also
		
		val nameStart = element.nameIdentifier?.textRange?.startOffset ?: return@also
		val nameEnd = caret.offset
		val spacedName = document.getText(nameStart..nameEnd).run {
			val i = indexOfAny(listOf("extends", "implements", "permits", "\n"))
			if (i != -1) substring(0, i) else this
		}
		val newName = spacedName.trim().split(' ').joinToString("") { it.titleCase } + " "
		
		project.runWriteCommandAction {
			document.replaceString(nameStart, nameStart + spacedName.length, newName)
		}
	}
}
