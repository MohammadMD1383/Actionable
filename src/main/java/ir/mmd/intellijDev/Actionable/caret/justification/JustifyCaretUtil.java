package ir.mmd.intellijDev.Actionable.caret.justification;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static ir.mmd.intellijDev.Actionable.util.Utility.repeat;

/**
 * This class is used to manipulate editor carets
 */
class JustifyCaretUtil {
	private final @NotNull Project project;
	private final @NotNull Editor editor;
	private final @NotNull Document document;
	private final @NotNull List<Caret> carets;
	
	// private List<CaretState> caretStates;
	
	public JustifyCaretUtil(
		@NotNull Project project,
		@NotNull Editor editor
	) {
		this.editor = editor;
		this.project = project;
		this.document = editor.getDocument();
		carets = editor.getCaretModel().getAllCarets();
	}
	
	// /**
	//  * saves current {@link CaretState}s to restore later
	//  */
	// public void backupCarets() {
	// 	caretStates = editor.getCaretModel().getCaretsAndSelections();
	// }
	
	// /**
	//  * restores {@link CaretState}s previously backed up by {@link JustifyCaretUtil#backupCarets()}
	//  */
	// public void restoreCarets() {
	// 	assert caretStates != null;
	// 	editor.getCaretModel().setCaretsAndSelections(caretStates);
	// }
	
	/**
	 * moves all carets to leftmost active column between carets <br><br>
	 * example: <br>
	 * <pre>
	 *     some text with car|et
	 *     another wi|th caret
	 *     the third one with a caret|
	 * </pre>
	 * will change to <br>
	 * <pre>
	 *     some text |with caret
	 *     another wi|th caret
	 *     the third |one with a caret
	 * </pre>
	 */
	public void justifyCaretsStart() {
		justify(getLeftmostColumn());
	}
	
	/**
	 * same as {@link JustifyCaretUtil#justifyCaretsStart()} but moves the carets to rightmost active column
	 */
	public void justifyCaretsEnd() {
		justify(getRightmostColumn());
	}
	
	/**
	 * aligns given carets across target column
	 *
	 * @param targetColumn the column that all carets will be aligned across to it
	 */
	private void justify(int targetColumn) {
		runWriteCommandAction(project, () -> {
			for (Caret caret : carets) {
				if (!caret.isValid()) continue;
				
				final int currentLine = caret.getLogicalPosition().line;
				final int lineEndOffset = document.getLineEndOffset(currentLine);
				final int lineLastColumn = editor.offsetToLogicalPosition(lineEndOffset).column;
				
				if (lineLastColumn < targetColumn)
					document.insertString(lineEndOffset, repeat(" ", targetColumn - lineLastColumn));
				
				caret.moveToLogicalPosition(
					new LogicalPosition(currentLine, targetColumn)
				);
			}
		});
	}
	
	/**
	 * moves all carets to rightmost active column between carets, and shifts the text<br><br>
	 * example: <br>
	 * <pre>
	 *     int short |= 12;
	 *     int mediumMedium |= 12;
	 *     int largeLargeLarge |= 12;
	 * </pre>
	 * will change to <br>
	 * <pre>
	 *     int short           |= 12;
	 *     int mediumMedium    |= 12;
	 *     int largeLargeLarge |= 12;
	 * </pre>
	 */
	public void justifyCaretsEndWithShifting() {
		if (hasMoreThanOneCaretOnOneLine()) return;
		
		final int targetColumn = getRightmostColumn();
		runWriteCommandAction(project, () -> {
			for (Caret caret : carets) {
				final int diff = targetColumn - caret.getLogicalPosition().column;
				document.insertString(caret.getOffset() - 1, repeat(" ", diff));
			}
		});
	}
	
	/**
	 * returns all {@link LogicalPosition}s of given carets as a {@link Stream}
	 *
	 * @return stream of {@link LogicalPosition}s from given carets
	 */
	private Stream<LogicalPosition> getAllCaretPositionsStream() { return carets.stream().map(Caret::getLogicalPosition); }
	
	/**
	 * returns the leftmost column position among given carets <br>
	 * please refer to {@link JustifyCaretUtil#justifyCaretsStart()} for details
	 *
	 * @return the leftmost column position
	 */
	private int getLeftmostColumn() {
		//noinspection OptionalGetWithoutIsPresent
		return getAllCaretPositionsStream().mapToInt(position -> position.column).min().getAsInt();
	}
	
	/**
	 * same as {@link JustifyCaretUtil#getLeftmostColumn()} but returns the rightmost column position
	 *
	 * @return the rightmost column position
	 */
	private int getRightmostColumn() {
		//noinspection OptionalGetWithoutIsPresent
		return getAllCaretPositionsStream().mapToInt(position -> position.column).max().getAsInt();
	}
	
	/**
	 * checks if there are more than on caret on a single line
	 *
	 * @return true if there are more than one caret on a single line, otherwise false
	 */
	private boolean hasMoreThanOneCaretOnOneLine() { return !getAllCaretPositionsStream().mapToInt(position -> position.line).allMatch(new HashSet<>()::add); }
}
