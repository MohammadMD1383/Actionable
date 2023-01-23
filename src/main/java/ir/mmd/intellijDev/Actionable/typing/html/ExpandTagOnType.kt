package ir.mmd.intellijDev.Actionable.typing.html

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.typing.html.state.State
import ir.mmd.intellijDev.Actionable.util.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.util.ext.getText
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction
import ir.mmd.intellijDev.Actionable.util.ext.service
import ir.mmd.intellijDev.Actionable.util.ext.util
import ir.mmd.intellijDev.Actionable.util.service

@Keep
class ExpandTagOnType : TypedHandlerDelegate() {
	companion object {
		private val TAGS = listOf(
			"abbr", "acronym", "address", "applet", "area",
			"article", "aside", "audio", "base", "basefont",
			"bdi", "bdo", "big", "blockquote", "body", "button",
			"canvas", "caption", "center", "cite", "code", "dd",
			"del", "details", "div", "dl", "dt", "figcaption",
			"figure", "footer", "form", "frame",
			"h1", "h2", "h3", "h4", "h5", "h6", "head", "header",
			"html", "iframe", "img", "input", "kbd", "label",
			"li", "mark", "nav", "noscript", "object", "ol",
			"option", "pre", "script", "section", "select",
			"small", "span", "strike", "strong", "style", "sub",
			"sup", "table", "tbody", "td", "textarea", "tfoot",
			"th", "thead", "title", "tr", "ul", "video"
		)
	}
	
	override fun charTyped(
		c: Char,
		project: Project,
		editor: Editor,
		file: PsiFile
	) = Result.CONTINUE.also {
		if (
			!project.service<State>().expandTagOnTypeEnabled ||
			file.fileType !is HtmlFileType
		) return@also
		
		val document = editor.document
		val caret = editor.caretModel.primaryCaret
		val offset = caret.offset
		val cutil = caret.util
		val movementSettings = service<SettingsState>()
		cutil.moveUntilReached(movementSettings.wordSeparators, movementSettings.hardStopCharacters, BACKWARD)
		val tag = document.getText(cutil.offset..offset)
		
		TAGS.find { it == tag }?.let { t ->
			project.runWriteCommandAction {
				document.deleteString(cutil.offset, offset)
				document.insertString(cutil.offset, "<$t></$t>")
				caret.moveToOffset(offset + 2)
			}
		}
	}
}
