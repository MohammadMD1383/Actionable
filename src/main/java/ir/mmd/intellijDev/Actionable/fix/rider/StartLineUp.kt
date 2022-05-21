package ir.mmd.intellijDev.Actionable.fix.rider

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.*

class StartLineUp : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		
		editor.caretModel.allCarets.asReversed().forEach { caret ->
			val line = caret.logicalPosition.line
			val lineStart = document.getLineStartOffset(line)
			val lineIndent = document.getLineIndent(line)
			
			e.project!!.runWriteCommandAction {
				document.insertString(lineStart, "${lineIndent}\n")
				caret.moveUp()
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
}