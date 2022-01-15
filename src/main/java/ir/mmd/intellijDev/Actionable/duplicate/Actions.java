package ir.mmd.intellijDev.Actionable.duplicate;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements actions required by:<br>
 * <ul>
 *     <li>{@link DuplicateLinesUp}</li>
 *     <li>{@link DuplicateLinesDown}</li>
 * </ul>
 */
public class Actions {
	
	/**
	 * whether to duplicate the selected lines up or down
	 */
	public enum Direction {
		UP,
		DOWN
	}
	
	/**
	 * implementation for:
	 * <ul>
	 *     <li>{@link DuplicateLinesUp}</li>
	 *     <li>{@link DuplicateLinesDown}</li>
	 * </ul>
	 *
	 * @param e   event of mentioned actions
	 * @param dir see {@link Actions.Direction}
	 */
	public static void duplicate(
		@NotNull AnActionEvent e,
		@NotNull Direction dir
	) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var document = editor.getDocument();
		
		// noinspection ConstantConditions
		final var duplicateUtil = new DuplicateUtil(project, editor, document);
		
		final TriConsumer<DuplicateUtil, Integer, Integer> duplicateMethod;
		if (dir == Direction.UP)
			duplicateMethod = DuplicateUtil::duplicateUp;
		else /* dir == Direction.DOWN */
			duplicateMethod = DuplicateUtil::duplicateDown;
		
		for (Caret caret : editor.getCaretModel().getAllCarets()) {
			duplicateMethod.accept(
				duplicateUtil,
				caret.getSelectionStart(),
				caret.getSelectionEnd()
			);
		}
	}
}
