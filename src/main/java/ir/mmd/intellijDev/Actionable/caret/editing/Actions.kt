package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.internal.doc.Documentation
import ir.mmd.intellijDev.Actionable.util.ext.enableIf


@Documentation(
	title = "Cut Word at Caret",
	description = "Cuts the word under the caret",
	example = """
		everything is clear! no need for an example :)
	"""
)
class CutWordAtCaret : CaretEditingAction() {
	override fun isDumbAware() = true
	context (LazyEventContext)
	override fun perform(caret: Caret) = copyWordAtCaret(true)
}

@Documentation(
	title = "Cut Element at Caret",
	description = "Cuts the psi element under the caret",
	example = """
		psi elements are defined by the language parser.
		for instance, given the following statement:
		```java
		String a = "some string literal";
		```
		we will have these psi elements:
		```
		String
		a
		=
		"some string literal"
		;
		```
	"""
)
class CutElementAtCaret : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = copyElementAtCaret(true)
}

@Documentation(
	title = "Copy Word at Caret",
	description = "Copies the word under the caret",
	example = """
		everything is clear! no need for an example :)
	"""
)
class CopyWordAtCaret : CaretEditingAction() {
	override fun isDumbAware() = true
	context(LazyEventContext)
	override fun perform(caret: Caret) = copyWordAtCaret(false)
}

@Documentation(
	title = "Copy Element at Caret",
	description = "Copies the psi element under the caret",
	example = """
		psi elements are defined by the language parser.
		for instance, given the following statement:
		```java
		String a = "some string literal";
		```
		we will have these psi elements:
		```
		String
		a
		=
		"some string literal"
		;
		```
	"""
)
class CopyElementAtCaret : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = copyElementAtCaret(false)
}


class SetWordCutPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("wd;ct")
}


class SetWordCopyPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("wd;cp")
}


class SetElementCutPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("el;ct")
}


class SetElementCopyPasteOffset : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = setPasteOffset("el;cp")
}


class CancelPasteAction : CaretEditingAction() {
	context(LazyEventContext)
	override fun perform(caret: Caret) = editor.removeScheduledPasteAction()
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor && editor.getUserData(scheduledPasteActionKind) != null }
}
