package ir.mmd.intellijDev.Actionable.selection.movement.box

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class MoveSelectionAction : AnAction() {
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
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject && hasEditor && allCarets.haveSelection }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

class MoveSelectionUp : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		moveSelectionsUp()
	}
}

class MoveSelectionDown : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		moveSelectionsDown()
	}
}

class MoveSelectionLeft : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		moveSelectionsLeft()
	}
}

class MoveSelectionRight : MoveSelectionAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		moveSelectionsRight()
	}
}
