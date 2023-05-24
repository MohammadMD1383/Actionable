package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiPlaceholder
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.elementAtOrBefore
import ir.mmd.intellijDev.Actionable.util.ext.removeCharAt
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction
import ir.mmd.intellijDev.Actionable.util.ext.util

class MacroTemplateDollarTypedHandler : TypedHandlerDelegate() {
	override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType) = Result.CONTINUE after {
		if (fileType != MacroTemplateFileType || c != '$') {
			return@after
		}
		
		val document = editor.document
		val caret = editor.caretModel.currentCaret
		val cutil = caret.util
		
		fun removeNextChar() {
			project.runWriteCommandAction {
				document.removeCharAt(cutil.nextCharOffset)
			}
		}
		
		if (file.findElementAt(cutil.prevCharOffset)?.elementType == MacroTemplateTypes.ESCAPE) {
			return@after
		}
		
		if (cutil.prevChar == '$' && cutil.nextChar == '$') {
			removeNextChar()
			return@after
		}
		
		if (file.elementAtOrBefore(caret, skipWhitespace = false)?.parentOfType<MacroTemplatePsiPlaceholder>() != null) {
			if (cutil.nextChar == '$' && cutil.prevChar.let { it != null && it.isUpperCase() }) {
				removeNextChar()
			}
			
			return@after
		}
		
		project.runWriteCommandAction {
			document.insertString(cutil.nextCharOffset, "$")
		}
	}
	
	override fun checkAutoPopup(charTyped: Char, project: Project, editor: Editor, file: PsiFile): Result {
		if (charTyped == '$') {
			AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, CompletionType.BASIC, null)
			return Result.STOP
		}
		
		return Result.CONTINUE
	}
}
