package ir.mmd.intellijDev.Actionable.caret.manipulation

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*

@Keep
class RemoveCaretsOnEmptyLinesAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		val caretModel = editor.caretModel
		
		caretModel.allCarets.forEach {
			val lineNumber = document.getLineNumber(it.offset)
			val startOffset = document.getLineStartOffset(lineNumber)
			val endOffset = document.getLineEndOffset(lineNumber)
			val line = document.getText(startOffset..endOffset)
			
			if (line.isBlank()) {
				caretModel.removeCaret(it)
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { caretCount > 1 } }
	override fun isDumbAware() = true
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
