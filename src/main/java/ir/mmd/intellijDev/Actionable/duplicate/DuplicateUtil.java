package ir.mmd.intellijDev.Actionable.duplicate;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public class DuplicateUtil {
	@NotNull private final Project project;
	@NotNull private final Editor editor;
	@NotNull private final Document document;
	
	public DuplicateUtil(
		@NotNull Project project,
		@NotNull Editor editor
	) {
		this.project = project;
		this.editor = editor;
		this.document = editor.getDocument();
	}
	
	public void duplicateUp() {
		final var duplicateString = '\n' + getDuplicateString();
		final var insertionOffset = document.getLineEndOffset(
			document.getLineNumber(
				editor.getSelectionModel().getSelectionEnd()
			)
		);
		
		WriteCommandAction.runWriteCommandAction(project, () -> document.insertString(insertionOffset, duplicateString));
	}
	
	public void duplicateDown() {
		final var duplicateString = getDuplicateString() + '\n';
		final var insertionOffset = document.getLineStartOffset(
			document.getLineNumber(
				editor.getSelectionModel().getSelectionStart()
			)
		);
		
		WriteCommandAction.runWriteCommandAction(project, () -> document.insertString(insertionOffset, duplicateString));
	}
	
	private @NotNull String getDuplicateString() {
		final var selectionModel = editor.getSelectionModel();
		int lineStartOffset;
		int lineEndOffset;
		
		if (selectionModel.hasSelection()) {
			final var selectionStartLine = document.getLineNumber(selectionModel.getSelectionStart());
			final var selectionEndLine = document.getLineNumber(selectionModel.getSelectionEnd());
			lineStartOffset = document.getLineStartOffset(selectionStartLine);
			lineEndOffset = document.getLineEndOffset(selectionEndLine);
		} else {
			final var caretOffset = editor.getCaretModel().getOffset();
			final var lineNumber = document.getLineNumber(caretOffset);
			lineStartOffset = document.getLineStartOffset(lineNumber);
			lineEndOffset = document.getLineEndOffset(lineNumber);
		}
		
		return document.getText(new TextRange(lineStartOffset, lineEndOffset));
	}
}
