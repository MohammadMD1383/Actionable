package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.util.ext.allCarets
import ir.mmd.intellijDev.Actionable.util.ext.caretCount
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasEditorWith

class UnselectFirstSelectionAction : AnAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.first { it.hasSelection() }.removeSelection()
	override fun update(e: AnActionEvent) = e.enableIf { hasEditorWith { caretCount > 1 } }
}

class UnselectLastSelectionAction : AnAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = e.allCarets.last { it.hasSelection() }.removeSelection()
	override fun update(e: AnActionEvent) = e.enableIf { hasEditorWith { caretCount > 1 } }
}
