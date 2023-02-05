package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.MultiCaretActionWithInitialization
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.internal.doc.Documentation
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withService


@Documentation(
	title = "Remove Duplicate Selections",
	description = "This action will remove duplicate selections (should be used with multi-caret mode)",
	example = """
		| symbol              | meaning         |
		|---------------------|-----------------|
		| `(`                 | selection start |
		| `)`                 | selection end   |
		| <code>&#124;</code> | caret           |
		
		having:
		```
		(text 1)|
		(text 2)|
		(text 1)|
		```
		will produce:
		```
		(text 1)|
		(text 2)|
		|
		```
	"""
)
class RemoveDuplicatesAction : MultiCaretActionWithInitialization<HashSet<String>>() {
	context(LazyEventContext)
	override fun initialize(): HashSet<String> = HashSet()
	
	context (LazyEventContext)
	override fun perform(caret: Caret) = withService<SettingsState, Unit> {
		val text = caret.selectedText!!
		data.find { it.equals(text, ignoreCase = !isCaseSensitive) }?.letOnly {
			project.runWriteCommandAction {
				document.deleteString(caret.selectionStart, caret.selectionEnd)
			}
		} ?: data.add(text)
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { caretCount > 1 && allCaretsHaveSelection } }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
