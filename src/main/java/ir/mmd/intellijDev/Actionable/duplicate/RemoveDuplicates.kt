package ir.mmd.intellijDev.Actionable.duplicate

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState
import ir.mmd.intellijDev.Actionable.internal.doc.Documentation
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.*
import ir.mmd.intellijDev.Actionable.util.withService

@Keep
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
class RemoveDuplicates : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = withService<SettingsState, Unit> {
		val editor = e.editor
		val document = editor.document
		val strings = HashSet<String>()
		
		e.project.runWriteCommandAction {
			editor.allCarets.forEach { caret ->
				val text = caret.selectedText!!
				strings.find { it.equals(text, ignoreCase = !isCaseSensitive) }?.letOnly {
					document.deleteString(caret.selectionStart, caret.selectionEnd)
				} ?: strings.add(text)
			}
		}
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { caretCount > 1 && allCaretsHasSelection } }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
