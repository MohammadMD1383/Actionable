package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.haveSelection
import ir.mmd.intellijDev.Actionable.util.ext.replaceSelectedText
import ir.mmd.intellijDev.Actionable.util.ext.toCaseStyleOf
import ir.mmd.intellijDev.Actionable.util.ext.withEachIndexed
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class PastePreservingCaseAction : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() {
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
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor and allCarets.haveSelection
}
