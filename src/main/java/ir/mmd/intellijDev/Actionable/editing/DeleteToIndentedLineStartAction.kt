package ir.mmd.intellijDev.Actionable.editing

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*

@Keep
class DeleteToIndentedLineStartAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		
		e.project.runWriteCommandAction {
			e.allCarets.forEach {
				val lineNumber = document.getLineNumber(it.offset)
				val lineStartOffset = document.getLineStartOffset(lineNumber)
				val lineEndOffset = document.getLineEndOffset(lineNumber)
				val lineIndentEndOffset = lineStartOffset + document.getLineStartIndentLength(lineNumber)
				val line = document.getText(lineStartOffset..lineEndOffset)
				
				if (line.isBlank()) {
					document.deleteString(lineStartOffset, lineEndOffset)
				} else if (it.offset > lineIndentEndOffset) {
					document.deleteString(lineIndentEndOffset, it.offset)
				}
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
