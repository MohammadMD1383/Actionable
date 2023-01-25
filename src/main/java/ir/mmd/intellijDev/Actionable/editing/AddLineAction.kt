package ir.mmd.intellijDev.Actionable.editing

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class AddLineAction(private val above: Boolean) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val editor = e.editor
		val document = editor.document
		
		e.project.runWriteCommandAction {
			e.allCarets.forEachWith(document) {
				val lineNumber = getLineNumber(it.offset)
				insertString(
					if (above) getLineStartOffset(lineNumber) else getLineEndOffset(lineNumber),
					"\n"
				)
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}


class AddLineAboveCaretWMAction : AddLineAction(true)


class AddLineBelowCaretWMAction : AddLineAction(false)
