package ir.mmd.intellijDev.Actionable.action

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import ir.mmd.intellijDev.Actionable.text.macro.MacroAction

val MacrosActionGroup by lazy {
	ActionManager.getInstance().getAction(Actions.Groups.MACROS) as DefaultActionGroup
}

fun ActionManager.registerMacro(name: String, macro: String) {
	val action = MacroAction(name, macro)
	registerAction("${Actions.MACRO_PREFIX}.$name", action)
	MacrosActionGroup.add(action)
}

fun ActionManager.unregisterMacro(name: String) {
	val actionId = "${Actions.MACRO_PREFIX}.$name"
	val action = getAction(actionId)!!
	
	unregisterAction(actionId)
	MacrosActionGroup.remove(action)
}

inline fun action(id: String): AnAction? = ActionManager.getInstance().getAction(id)
