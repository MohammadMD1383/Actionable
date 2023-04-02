package ir.mmd.intellijDev.Actionable.text

import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.ui.showInputDialog
import ir.mmd.intellijDev.Actionable.util.ext.haveSelection
import ir.mmd.intellijDev.Actionable.util.ext.replaceSelectedText
import ir.mmd.intellijDev.Actionable.util.ext.toCaseStyleOf
import ir.mmd.intellijDev.Actionable.util.ext.withEach

class ReplaceSelectionsPreservingCaseAction : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() {
		val text = showInputDialog(project, "Replace Selections Preserving Case")
		if (text.isNullOrBlank()) {
			return
		}
		
		runWriteCommandAction {
			allCarets.withEach {
				replaceSelectedText { text toCaseStyleOf it }
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor and allCarets.haveSelection
}
