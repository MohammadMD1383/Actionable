package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.*
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class PastePreservingCaseAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String).split('\n')
		val contentsSize = contents.size
		
		if (contentsSize == 0) {
			return
		}
		
		runWriteCommandAction {
			if (caretCount > contentsSize) {
				contents.forEachIndexed { i, content ->
					allCarets[i].replaceSelectedText { content toCaseStyleOf it }
				}
			} else {
				allCarets.withEachIndexed { i ->
					replaceSelectedText { contents[i] toCaseStyleOf it }
				}
			}
		}
	}
	
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor and allCarets.haveSelection }
}
