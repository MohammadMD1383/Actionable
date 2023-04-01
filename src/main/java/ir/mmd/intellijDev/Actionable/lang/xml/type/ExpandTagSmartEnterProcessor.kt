package ir.mmd.intellijDev.Actionable.lang.xml.type

import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessor
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import ir.mmd.intellijDev.Actionable.lang.xml.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.ext.removeCharAt
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction

class ExpandTagSmartEnterProcessor : SmartEnterProcessor() {
	override fun process(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
		if (!project.service<SettingsState>().expandTagOnSmartEnterEnabled) {
			return false
		}
		
		val caret = editor.caretModel.primaryCaret
		val element = psiFile.findElementAt(caret.offset - 1) ?: return false
		
		if (element is XmlToken && element.tokenType == XmlTokenType.XML_EMPTY_ELEMENT_END) {
			project.runWriteCommandAction {
				val document = editor.document
				val slashOffset = element.textRange.endOffset - 2
				
				document.removeCharAt(slashOffset)
				document.insertString(caret.offset, "</${(element.parent as XmlTag).name}>")
			}
			
			return true
		}
		
		return false
	}
}
