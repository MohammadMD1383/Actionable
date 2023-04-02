package ir.mmd.intellijDev.Actionable.selection.movement.box

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class MoveSelectionAction : ActionBase(), DumbAware {
	context (LazyEventContext)
	private fun swap(
		caret: Caret,
		targetLine: Int,
		targetCol: Int,
		startCol: Int,
		endCol: Int,
		start: Int,
		end: Int
	) {
		val replacementStart = editor.logicalPositionToOffset(LogicalPosition(targetLine, startCol))
		val replacementEnd = editor.logicalPositionToOffset(LogicalPosition(targetLine, endCol))
		val replacementStr = document.getText(TextRange(replacementStart, replacementEnd))
		
		runWriteCommandAction {
			document.replaceString(replacementStart, replacementEnd, caret.selectedText!!)
			document.replaceString(start, end, replacementStr)
			caret moveTo LogicalPosition(targetLine, targetCol)
			caret.setSelection(replacementStart, replacementEnd)
		}
	}
	
	context (LazyEventContext)
	fun moveSelectionsUp() {
		allCarets.forEachIndexed { i, caret ->
			val (line, column) = caret.logicalPosition
			val upperLine = line - 1
			
			if (
				line == 0 ||
				allCarets.getOrNull(i - 1)?.logicalPosition?.line == upperLine
			) return@forEachIndexed
			
			var (start, end) = caret.selectionRange
			val startCol = editor.offsetToLogicalPosition(start).column
			val endCol = editor.offsetToLogicalPosition(end).column
			val upperLineEnd = document.getLineEndOffset(upperLine)
			val upperLastCol = editor.offsetToLogicalPosition(upperLineEnd).column
			
			if (upperLastCol < endCol) project.runWriteCommandAction {
				val diff = endCol - upperLastCol
				document.insertString(upperLineEnd, " ".repeat(diff))
				start += diff
				end += diff
			}
			
			swap(caret, upperLine, column, startCol, endCol, start, end)
		}
	}
	
	context (LazyEventContext)
	fun moveSelectionsDown() {
		allCarets.asReversed().forEach { caret ->
			val (line, column) = caret.logicalPosition
			val bottomLine = line + 1
			
			val (start, end) = caret.selectionRange
			val startCol = editor.offsetToLogicalPosition(start).column
			val endCol = editor.offsetToLogicalPosition(end).column
			
			if (document.lineCount == bottomLine) project.runWriteCommandAction {
				document.insertString(document.textLength, "\n" + " ".repeat(endCol))
			}
			
			val bottomLineEnd = document.getLineEndOffset(bottomLine)
			val bottomLastCol = editor.offsetToLogicalPosition(bottomLineEnd).column
			
			if (bottomLastCol < endCol) project.runWriteCommandAction {
				val diff = endCol - bottomLastCol
				document.insertString(bottomLineEnd, " ".repeat(diff))
			}
			
			swap(caret, bottomLine, column, startCol, endCol, start, end)
		}
	}
	
	context (LazyEventContext)
	fun moveSelectionsLeft() {
		allCarets.forEachIndexed { i, caret ->
			val (start, end) = caret.selectionRange
			val char = document.charAtOrNull(start - 1) ?: return@forEachIndexed
			
			if (
				char == '\n' ||
				start in allCarets.getOrNull(i - 1)?.selectionRange
			) return@forEachIndexed
			
			runWriteCommandAction {
				val newOffset = caret.offset - 1
				
				document.insertString(end, char.toString())
				document.deleteString(start - 1, start)
				caret moveTo newOffset
			}
		}
	}
	
	context (LazyEventContext)
	fun moveSelectionsRight() {
		allCarets.asReversed().forEach { caret ->
			val (start, end) = caret.selectionRange
			val char = document.charAtOrNull(end)
			val newOffset = caret.offset + 1
			
			runWriteCommandAction {
				if (char == null || char == '\n') {
					document.insertString(start, " ")
				} else {
					document.deleteString(end, end + 1)
					document.insertString(start, char.toString())
				}
				
				caret moveTo newOffset
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && allCarets.haveSelection
}

class MoveSelectionUp : MoveSelectionAction() {
	context (LazyEventContext)
	override fun performAction() = moveSelectionsUp()
}

class MoveSelectionDown : MoveSelectionAction() {
	context (LazyEventContext)
	override fun performAction() = moveSelectionsDown()
}

class MoveSelectionLeft : MoveSelectionAction() {
	context (LazyEventContext)
	override fun performAction() = moveSelectionsLeft()
}

class MoveSelectionRight : MoveSelectionAction() {
	context (LazyEventContext)
	override fun performAction() = moveSelectionsRight()
}
