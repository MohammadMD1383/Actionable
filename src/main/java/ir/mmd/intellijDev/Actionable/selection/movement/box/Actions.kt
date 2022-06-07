package ir.mmd.intellijDev.Actionable.selection.movement.box

import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.editor

@Keep
class MoveSelectionUp : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsUp()
}

@Keep
class MoveSelectionDown : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsDown()
}

@Keep
class MoveSelectionLeft : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsLeft()
}

@Keep
class MoveSelectionRight : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsRight()
}
