package ir.mmd.intellijDev.Actionable.action

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.extensions.PluginId
import ir.mmd.intellijDev.Actionable.app.Actionable
import ir.mmd.intellijDev.Actionable.text.macro.MacroAction

/**
 * Returns `MacrosActionGroup` as a [DefaultActionGroup] to be used for adding custom macros
 */
val MacrosActionGroup by lazy {
	ActionManager.getInstance().getAction(Actions.Groups.MACROS) as DefaultActionGroup
}

/**
 * Registers a macro
 */
fun ActionManager.registerMacro(name: String, macro: String) {
	val action = MacroAction(name, macro)
	registerAction(
		"${Actions.MACRO_PREFIX}.$name",
		action,
		PluginId.getId(Actionable.PLUGIN_ID)
	)
	MacrosActionGroup.add(action)
}

/**
 * Unregisters a macro
 */
fun ActionManager.unregisterMacro(actionId: String) {
	val action = getAction(actionId)!!
	
	unregisterAction(actionId)
	MacrosActionGroup.remove(action)
}

/**
 * Retrieves [AnAction] from its id
 */
@Suppress("NOTHING_TO_INLINE")
inline fun action(id: String): AnAction? = ActionManager.getInstance().getAction(id)

/**
 * Checks whether all carets, and their selections are on a single line or not.
 */
context (LazyEventContext)
fun caretsAndSelectionsAreOnTheSameLine(): Boolean {
	val distinctLines = allCarets.map { it.logicalPosition.line }.distinct()
	if (distinctLines.size != 1) {
		return false
	}
	
	val line = distinctLines.first()
	return !(document.getLineNumber(allCarets.first().selectionStart) != line ||
		document.getLineNumber(allCarets.last().selectionEnd) != line)
}
