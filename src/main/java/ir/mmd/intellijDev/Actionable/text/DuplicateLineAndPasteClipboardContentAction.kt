package ir.mmd.intellijDev.Actionable.text

import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.internal.doc.Documentation
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

@Documentation(
	title = "Duplicate Line and Paste Contents from Clipboard",
	description = "Duplicates line under caret for each line of clipboard content and pastes that line at current caret position.",
	example = """
		| symbol              | meaning |
		| ------------------- | ------- |
		| <code>&#124;</code> | caret   |

		copy/have in clipboard:
		```java
		A
		B
		C
		```
		having:
		```java
		String s| = "some string";
		```
		executing action will produce:
		```java
		String sA| = "some string";
		String sB| = "some string";
		String sC| = "some string";
		```

		Note: If any text is selected, it will be replaced with pasted line's value.
	"""
)
class DuplicateLineAndPasteClipboardContentAction : DuplicateLineAndInsertContent() {
	context(LazyEventContext)
	override fun getReplacements(): MutableList<String>? {
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String)
			.split("\n").toMutableList()
		
		return contents.ifEmpty { null }
	}
}
