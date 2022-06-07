package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import ir.mmd.intellijDev.Actionable.action.registerMacro
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.text.macro.settings.SettingsState
import ir.mmd.intellijDev.Actionable.util.after
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasEditorWith
import ir.mmd.intellijDev.Actionable.util.ext.primaryCaret
import ir.mmd.intellijDev.Actionable.util.withService

@Keep
class AddSelectionToMacrosAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = withService<SettingsState, Unit> {
		val project = e.project
		
		val macroName = Messages.showInputDialog(
			project,
			"Enter a name for the macro",
			"Add Selection To Macros",
			null
		) ?: return@withService
		
		if (macros[macroName] != null) return@withService after {
			Messages.showErrorDialog(
				project,
				"A macro with the name '$macroName' already exists",
				"Add Selection To Macros"
			)
		}
		
		val macro = e.primaryCaret.selectedText!!
		macros[macroName] = macro
		ActionManager.getInstance().registerMacro(macroName, macro)
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasEditorWith { caretModel.run { caretCount == 1 && primaryCaret.hasSelection() } } }
}
