package ir.mmd.intellijDev.Actionable.selection.movement.box

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import ir.mmd.intellijDev.Actionable.util.ext.*

abstract class MoveSelectionAction : AnAction() {
	private fun swap(
		project: Project,
		editor: Editor,
		document: Document,
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
		
		project.runWriteCommandAction {
			document.replaceString(replacementStart, replacementEnd, caret.selectedText!!)
			document.replaceString(start, end, replacementStr)
			caret moveTo LogicalPosition(targetLine, targetCol)
			caret.setSelection(replacementStart, replacementEnd)
		}
	}
	
	protected fun Editor.moveSelectionsUp() {
		val project = project!!
		val carets = allCarets
		val document = document
		
		carets.forEachIndexed { i, caret ->
			val (line, column) = caret.logicalPosition
			val upperLine = line - 1
			
			if (
				line == 0 ||
				carets.getOrNull(i - 1)?.logicalPosition?.line == upperLine
			) return@forEachIndexed
			
			var (start, end) = caret.selectionRange
			val startCol = offsetToLogicalPosition(start).column
			val endCol = offsetToLogicalPosition(end).column
			val upperLineEnd = document.getLineEndOffset(upperLine)
			val upperLastCol = offsetToLogicalPosition(upperLineEnd).column
			
			if (upperLastCol < endCol) project.runWriteCommandAction {
				val diff = endCol - upperLastCol
				document.insertString(upperLineEnd, " ".repeat(diff))
				start += diff
				end += diff
			}
			
			swap(project, this, document, caret, upperLine, column, startCol, endCol, start, end)
		}
	}
	
	protected fun Editor.moveSelectionsDown() {
		val project = project!!
		val document = document
		
		allCarets.asReversed().forEach { caret ->
			val (line, column) = caret.logicalPosition
			val bottomLine = line + 1
			
			val (start, end) = caret.selectionRange
			val startCol = offsetToLogicalPosition(start).column
			val endCol = offsetToLogicalPosition(end).column
			
			if (document.lineCount == bottomLine) project.runWriteCommandAction {
				document.insertString(document.textLength, "\n" + " ".repeat(endCol))
			}
			
			val bottomLineEnd = document.getLineEndOffset(bottomLine)
			val bottomLastCol = offsetToLogicalPosition(bottomLineEnd).column
			
			if (bottomLastCol < endCol) project.runWriteCommandAction {
				val diff = endCol - bottomLastCol
				document.insertString(bottomLineEnd, " ".repeat(diff))
			}
			
			swap(project, this, document, caret, bottomLine, column, startCol, endCol, start, end)
		}
	}
	
	protected fun Editor.moveSelectionsLeft() {
		val carets = allCarets
		val document = document
		
		carets.forEachIndexed { i, caret ->
			val (start, end) = caret.selectionRange
			val char = document.charAtOrNull(start - 1) ?: return@forEachIndexed
			
			if (
				char == '\n' ||
				start in carets.getOrNull(i - 1)?.selectionRange
			) return@forEachIndexed
			
			project!!.runWriteCommandAction {
				val newOffset = caret.offset - 1
				
				document.insertString(end, char.toString())
				document.deleteString(start - 1, start)
				caret moveTo newOffset
			}
		}
	}
	
	protected fun Editor.moveSelectionsRight() {
		val document = document
		
		allCarets.asReversed().forEach { caret ->
			val (start, end) = caret.selectionRange
			val char = document.charAtOrNull(end)
			val newOffset = caret.offset + 1
			
			project!!.runWriteCommandAction {
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
