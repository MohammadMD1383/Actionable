package ir.mmd.intellijDev.Actionable.caret.justification;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

/**
 * This class is used to manipulate editor carets
 */
class JustifyCaretUtil {
	private final @NotNull Editor editor;
	private final @NotNull Project project;
	
	private List<CaretState> caretStates;
	
	public JustifyCaretUtil(
		@NotNull Project project,
		@NotNull Editor editor
	) {
		this.editor = editor;
		this.project = project;
	}
	
	/**
	 * saves current {@link CaretState}s to restore later
	 */
	public void backupCarets() {
		caretStates = editor.getCaretModel().getCaretsAndSelections();
	}
	
	/**
	 * restores {@link CaretState}s previously backed up by {@link JustifyCaretUtil#backupCarets()}
	 */
	public void restoreCarets() {
		assert caretStates != null;
		editor.getCaretModel().setCaretsAndSelections(caretStates);
	}
	
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
		final List<Caret> carets = editor.getCaretModel().getAllCarets();
		final int targetColumn = getStartColumn(carets);
		
		justify(carets, targetColumn);
	}
	
	/**
	 * same as {@link JustifyCaretUtil#justifyCaretsStart()} but moves the carets to rightmost active column
	 */
	public void justifyCaretsEnd() {
		final List<Caret> carets = editor.getCaretModel().getAllCarets();
		final int targetColumn = getEndColumn(carets);
		
		justify(carets, targetColumn);
	}
	
	/**
	 * aligns given carets across target column
	 *
	 * @param carets       carets to be aligned
	 * @param targetColumn the column that all carets will be aligned across to it
	 */
	private void justify(
		List<Caret> carets,
		int targetColumn
	) {
		WriteCommandAction.runWriteCommandAction(project, () -> {
			for (Caret caret : carets) {
				final int currentLine = caret.getLogicalPosition().line;
				caret.moveToLogicalPosition(
					new LogicalPosition(currentLine, targetColumn)
				);
			}
		});
	}
	
	/**
	 * returns all {@link LogicalPosition}s of given carets as a {@link Stream}
	 *
	 * @param carets list of all carets
	 * @return stream of {@link LogicalPosition}s from given carets
	 */
	private Stream<LogicalPosition> getAllCaretPositionsStream(@NotNull List<Caret> carets) {
		return carets.stream().map(Caret::getLogicalPosition);
	}
	
	/**
	 * returns the leftmost column position among given carets <br>
	 * please refer to {@link JustifyCaretUtil#justifyCaretsStart()} for details
	 *
	 * @param carets list of all carets
	 * @return the leftmost column position
	 */
	private int getStartColumn(@NotNull List<Caret> carets) {
		//noinspection OptionalGetWithoutIsPresent
		return getAllCaretPositionsStream(carets).mapToInt(position -> position.column).min().getAsInt();
	}
	
	/**
	 * same as {@link JustifyCaretUtil#getStartColumn(List)} but returns the rightmost column position
	 *
	 * @param carets list of all carets
	 * @return the rightmost column position
	 */
	private int getEndColumn(@NotNull List<Caret> carets) {
		//noinspection OptionalGetWithoutIsPresent
		return getAllCaretPositionsStream(carets).mapToInt(position -> position.column).max().getAsInt();
	}
}
