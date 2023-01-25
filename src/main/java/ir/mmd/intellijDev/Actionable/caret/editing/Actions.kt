package ir.mmd.intellijDev.Actionable.caret.editing

import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.doc.Documentation

import ir.mmd.intellijDev.Actionable.util.ext.*


@Documentation(
	title = "Cut Word at Caret",
	description = "Cuts the word under the caret",
	example = """
		everything is clear! no need for an example :)
	"""
)
class CutWordAtCaret : CaretEditingAction() {
	override fun isDumbAware() = true
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, true)
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
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, true)
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
	override fun actionPerformed(e: AnActionEvent) = copyWordAtCaret(e, false)
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
	override fun actionPerformed(e: AnActionEvent) = copyElementAtCaret(e, false)
}


class SetWordCutPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;ct")
}


class SetWordCopyPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "wd;cp")
}


class SetElementCutPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;ct")
}


class SetElementCopyPasteOffset : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = setPasteOffset(e, "el;cp")
}


class CancelPasteAction : CaretEditingAction() {
	override fun actionPerformed(e: AnActionEvent) = e.editor.removeScheduledPasteAction()
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditorWith { getUserData(scheduledPasteActionKind).isNotNull } }
}
