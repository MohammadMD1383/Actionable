package ir.mmd.intellijDev.Actionable.text.macro.lang

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplatePsiPlaceholder
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes
import ir.mmd.intellijDev.Actionable.util.ext.elementAt
import ir.mmd.intellijDev.Actionable.util.ext.removeCharAt
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction
import ir.mmd.intellijDev.Actionable.util.ext.util

class MacroTemplateDollarBackspaceHandler : BackspaceHandlerDelegate() {
	override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {
		if (file.fileType != MacroTemplateFileType || c != '$') {
			return
		}
		
		val project = editor.project
		val document = editor.document
		val caret = editor.caretModel.currentCaret
		val cutil = caret.util
		
		if (cutil.prevChar == '$' && cutil.nextChar == '$') {
			if (file.findElementAt(cutil.prevCharOffset)?.elementType != MacroTemplateTypes.DOLLAR) {
				return
			}
			
			if (file.elementAt(caret)?.parentOfType<MacroTemplatePsiPlaceholder>() != null) {
				return
			}
			
			project.runWriteCommandAction {
				document.removeCharAt(cutil.nextCharOffset)
			}
		}
	}
	
	override fun charDeleted(c: Char, file: PsiFile, editor: Editor) = false
}
