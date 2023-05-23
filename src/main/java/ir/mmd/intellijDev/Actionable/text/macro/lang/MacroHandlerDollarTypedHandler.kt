package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.removeCharAt
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction
import ir.mmd.intellijDev.Actionable.util.ext.util

class MacroHandlerDollarTypedHandler : TypedHandlerDelegate() {
	override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType) = Result.CONTINUE after {
		if (c != '$') {
			return@after
		}
		
		val document = editor.document
		
		editor.caretModel.allCarets.forEach {
			val cutil = it.util
			
			if (cutil.nextChar == '$') {
				if (cutil.prevChar == '$') {
					project.runWriteCommandAction {
						document.removeCharAt(cutil.nextCharOffset)
					}
				} else {
					return@forEach
				}
			}
			
			project.runWriteCommandAction {
				document.insertString(cutil.nextCharOffset, "$")
			}
			
			AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, CompletionType.BASIC, null)
		}
	}
}
