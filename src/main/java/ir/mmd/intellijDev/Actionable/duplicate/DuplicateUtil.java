package ir.mmd.intellijDev.Actionable.duplicate;

import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;

/**
 * This class is used to duplicate line(s) in the editor
 */
class DuplicateUtil {
	@NotNull private final Project project;
	@NotNull private final Document document;
	@NotNull private final Editor editor;
	
	public DuplicateUtil(
		@NotNull Project project,
		@NotNull Editor editor,
		@NotNull Document document
	) {
		this.project = project;
		this.editor = editor;
		this.document = document;
	}
	
	/**
	 * duplicate line(s) up <br>
	 *
	 * <ul>
	 *   <li>if <code>start offset</code> and <code>end offset</code> are same, then the line will be duplicated <br></li>
	 *   <li>if not, then the <b>lines including start offset and end offset</b> will be duplicated</li>
	 * </ul>
	 *
	 * <i>start offset and end offset are caret offsets in the active editor document</i>
	 *
	 * @param start start offset of duplication range
	 * @param end   end offset of duplication range
	 */
	public void duplicateUp(
		int start,
		int end
	) {
		final DuplicateString duplicateString = getDuplicateString(start, end);
		
		runWriteCommandAction(project, () ->
			document.insertString(duplicateString.lineEndOffset, '\n' + duplicateString.text)
		);
	}
	
	/**
	 * please refer to {@link DuplicateUtil#duplicateUp(int, int)} for documentation
	 */
	public void duplicateDown(
		int start,
		int end
	) {
		final DuplicateString duplicateString = getDuplicateString(start, end);
		
		runWriteCommandAction(project, () -> {
			document.insertString(duplicateString.lineStartOffset, duplicateString.text + '\n');
			
			// check if the caret is at line start,
			// then move the caret manually.
			// due to an issue that caret won't move automatically.
			// to be more clear, you can comment out the statements below and see the effect.
			// put the caret at the line start and fire the `duplicate down` action.
			if (duplicateString.lineStartOffset == start) {
				final Caret caret = editor.getCaretModel().getCaretAt(new VisualPosition(duplicateString.selectionStartLine, 0));
				if (caret == null) return;
				
				caret.moveToLogicalPosition(new LogicalPosition(
					duplicateString.selectionStartLine + (duplicateString.selectionEndLine - duplicateString.selectionStartLine) + 1,
					0
				));
			}
		});
	}
	
	/**
	 * retrieves the line(s) of the document to be duplicated <br>
	 * more info at {@link DuplicateUtil#duplicateUp(int, int)}
	 *
	 * @param start starting caret offset of duplication range
	 * @param end   ending caret offset of duplication range
	 * @return selected line(s) for duplication
	 */
	private @NotNull DuplicateString getDuplicateString(
		int start,
		int end
	) {
		int selectionStartLine;
		int selectionEndLine;
		int lineStartOffset;
		int lineEndOffset;
		
		if (start != end) /* has selection */ {
			selectionStartLine = document.getLineNumber(start);
			selectionEndLine = document.getLineNumber(end);
			lineStartOffset = document.getLineStartOffset(selectionStartLine);
			lineEndOffset = document.getLineEndOffset(selectionEndLine);
		} else /* no selection */ {
			selectionStartLine = selectionEndLine = document.getLineNumber(start);
			lineStartOffset = document.getLineStartOffset(selectionStartLine);
			lineEndOffset = document.getLineEndOffset(selectionStartLine);
		}
		
		return new DuplicateString(
			selectionStartLine,
			selectionEndLine,
			lineStartOffset,
			lineEndOffset,
			document.getText(new TextRange(lineStartOffset, lineEndOffset))
		);
	}
	
	/**
	 * <b>data class</b> <br>
	 * this class contains information about the text that is going to be duplicated
	 */
	private static class DuplicateString {
		private final int selectionStartLine;
		private final int selectionEndLine;
		private final int lineStartOffset;
		private final int lineEndOffset;
		@NotNull private final String text;
		
		public DuplicateString(
			int selectionStartLine,
			int selectionEndLine,
			int lineStartOffset,
			int lineEndOffset,
			@NotNull String text
		) {
			this.selectionStartLine = selectionStartLine;
			this.selectionEndLine = selectionEndLine;
			this.lineStartOffset = lineStartOffset;
			this.lineEndOffset = lineEndOffset;
			this.text = text;
		}
	}
}
