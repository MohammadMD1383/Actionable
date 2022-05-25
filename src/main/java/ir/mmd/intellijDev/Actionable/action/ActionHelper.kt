package ir.mmd.intellijDev.Actionable.action

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction

fun action(id: String): AnAction {
	return ActionManager.getInstance().getAction(id)
}
