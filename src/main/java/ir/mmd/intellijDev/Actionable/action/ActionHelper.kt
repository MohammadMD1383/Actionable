package ir.mmd.intellijDev.Actionable.action

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import ir.mmd.intellijDev.Actionable.text.macro.MacroAction

/**
 * Returns `MacrosActionGroup` as a [DefaultActionGroup] to be used for adding custom macros
 *
 * @see ir.mmd.intellijDev.Actionable.text.macro.AddSelectionToMacrosAction
 */
val MacrosActionGroup by lazy {
	ActionManager.getInstance().getAction(Actions.Groups.MACROS) as DefaultActionGroup
}

/**
 * Registers a macro
 *
 * @see ir.mmd.intellijDev.Actionable.text.macro.AddSelectionToMacrosAction
 */
fun ActionManager.registerMacro(name: String, macro: String) {
	val action = MacroAction(name, macro)
	registerAction("${Actions.MACRO_PREFIX}.$name", action)
	MacrosActionGroup.add(action)
}

/**
 * Unregisters a macro
 *
 * @see ir.mmd.intellijDev.Actionable.text.macro.AddSelectionToMacrosAction
 */
fun ActionManager.unregisterMacro(name: String) {
	val actionId = "${Actions.MACRO_PREFIX}.$name"
	val action = getAction(actionId)!!
	
	unregisterAction(actionId)
	MacrosActionGroup.remove(action)
}

/**
 * Retrieves [AnAction] from its id
 */
@Suppress("NOTHING_TO_INLINE")
inline fun action(id: String): AnAction? = ActionManager.getInstance().getAction(id)
