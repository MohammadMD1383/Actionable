package ir.mmd.intellijDev.Actionable.text

import ir.mmd.intellijDev.Actionable.action.LazyEventContext

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class DuplicateLineAndPasteClipboardContentAction : DuplicateLineAndInsertContent() {
	context(LazyEventContext)
	override fun getReplacements(): MutableList<String>? {
		val contents = (Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String)
			.split("\n").toMutableList()
		
		return contents.ifEmpty { null }
	}
}
