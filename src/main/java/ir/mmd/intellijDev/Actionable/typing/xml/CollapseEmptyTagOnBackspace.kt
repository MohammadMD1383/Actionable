package ir.mmd.intellijDev.Actionable.typing.xml

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import ir.mmd.intellijDev.Actionable.typing.xml.state.State
import ir.mmd.intellijDev.Actionable.util.ext.moveTo
import ir.mmd.intellijDev.Actionable.util.ext.nextLeafNoWhitespace
import ir.mmd.intellijDev.Actionable.util.ext.prevLeafNoWhitespace
import ir.mmd.intellijDev.Actionable.util.ext.util

class CollapseEmptyTagOnBackspace : BackspaceHandlerDelegate() {
	private var caretOffset: Int? = null
	
	override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {
		if (
			file.fileType !is XmlFileType ||
			c != '>'
		) return
		
		val project = editor.project!!
		if (!project.service<State>().collapseEmptyTagOnBackspaceEnabled) return
		
		val caret = editor.caretModel.primaryCaret
		val offset = caret.offset
		val baseElement = file.findElementAt(offset) ?: return
		
		val prevElement = baseElement
			.prevLeafNoWhitespace(true)
			?.parentOfType<XmlTag>(true)
			?: return
		val nextElement = baseElement
			.nextLeafNoWhitespace(true)
			?.parentOfType<XmlTag>(true)
			?: return
		if (prevElement != nextElement) return
		
		val cutil = caret.util
		if (
			cutil.nextChar == '<' &&
			prevElement.subTags.isEmpty() &&
			prevElement.children.any { it is XmlToken && it.tokenType == XmlTokenType.XML_END_TAG_START }
		) {
			caretOffset = offset
			caret moveTo 0
			prevElement.collapseIfEmpty()
			PsiDocumentManager
				.getInstance(project)
				.doPostponedOperationsAndUnblockDocument(editor.document)
		}
	}
	
	override fun charDeleted(c: Char, file: PsiFile, editor: Editor): Boolean {
		if (caretOffset != null) {
			editor.caretModel.primaryCaret moveTo caretOffset!! + 1
			caretOffset = null
		}
		return false
	}
}
