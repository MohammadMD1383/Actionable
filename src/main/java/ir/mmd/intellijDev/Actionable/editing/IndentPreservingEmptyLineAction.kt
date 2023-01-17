package ir.mmd.intellijDev.Actionable.editing

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*

@Keep
class IndentPreservingEmptyLineAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		
		e.allCarets.forEach {
			val lineNumber = document.getLineNumber(it.offset)
			val lineStartOffset = document.getLineStartOffset(lineNumber)
			val lineEndOffset = document.getLineEndOffset(lineNumber)
			val lineIndentEndOffset = lineStartOffset + document.getLineIndentCharCount(lineNumber)
			
			it moveTo lineIndentEndOffset
			e.project.runWriteCommandAction {
				document.deleteString(lineIndentEndOffset, lineEndOffset)
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}
