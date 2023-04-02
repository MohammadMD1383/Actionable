package ir.mmd.intellijDev.Actionable.caret.justification

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.forEachIf
import ir.mmd.intellijDev.Actionable.util.ext.moveTo

abstract class JustifyAction : ActionBase(), DumbAware {
	context (LazyEventContext)
	protected fun getLeftmostColumn() = allCarets.minOf { it.visualPosition.column }
	
	context (LazyEventContext)
	protected fun getRightmostColumn() = allCarets.maxOf { it.logicalPosition.column }
	
	/**
	 * aligns given carets across target column
	 *
	 * @param targetColumn the column that all carets will be aligned across to it
	 */
	context (LazyEventContext)
	protected fun justify(targetColumn: Int) = runWriteCommandAction {
		allCarets.forEachIf({ isValid }) {
			val currentLine = it.logicalPosition.line
			val lineEndOffset = document.getLineEndOffset(currentLine)
			val lineLastColumn = editor.offsetToLogicalPosition(lineEndOffset).column
			if (lineLastColumn < targetColumn) document.insertString(lineEndOffset, " ".repeat(targetColumn - lineLastColumn))
			it moveTo LogicalPosition(currentLine, targetColumn)
		}
	}
	
	/**
	 * Moves all carets to rightmost active column between carets, and shifts the text
	 *
	 * Example:
	 *
	 * ```java
	 * int short |= 12;
	 * int mediumMedium |= 12;
	 * int largeLargeLarge |= 12;
	 * ```
	 *
	 * will change to <br></br>
	 *
	 * ```java
	 * int short           |= 12;
	 * int mediumMedium    |= 12;
	 * int largeLargeLarge |= 12;
	 * ```
	 */
	context (LazyEventContext)
	protected fun justifyCaretsEndWithShifting() {
		if (allCarets.distinctBy { it.logicalPosition.line }.size != allCarets.size) {
			return
		}
		
		val targetColumn = getRightmostColumn()
		runWriteCommandAction {
			allCarets.forEach {
				document.insertString(
					it.offset,
					" ".repeat(targetColumn - it.logicalPosition.column)
				)
				it.moveToLogicalPosition(LogicalPosition(it.logicalPosition.line, targetColumn))
			}
		}
	}
	
	context(LazyEventContext)
	override fun isEnabled() = hasEditor && caretCount > 1
}

class JustifyCaretsEndAndShift : JustifyAction() {
	context(LazyEventContext)
	override fun performAction() = justify(getRightmostColumn())
}

class JustifyCaretsStart : JustifyAction() {
	context(LazyEventContext)
	override fun performAction() = justify(getLeftmostColumn())
}

class JustifyCaretsEnd : JustifyAction() {
	context(LazyEventContext)
	override fun performAction() = justifyCaretsEndWithShifting()
}
