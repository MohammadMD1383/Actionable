package ir.mmd.intellijDev.Actionable.selection.movement.box

import com.intellij.openapi.actionSystem.AnActionEvent

import ir.mmd.intellijDev.Actionable.util.ext.editor


class MoveSelectionUp : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsUp()
}


class MoveSelectionDown : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsDown()
}


class MoveSelectionLeft : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsLeft()
}


class MoveSelectionRight : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.moveSelectionsRight()
}
