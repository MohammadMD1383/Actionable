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
	 * implementation for:
	 * <ul>
	 *     <li>{@link DuplicateLinesUp}</li>
	 *     <li>{@link DuplicateLinesDown}</li>
	 * </ul>
	 *
	 * @param e   event of mentioned actions
	 * @param duplicator a method reference from {@link DuplicateUtil} class
	 */
	public static void duplicate(
		@NotNull AnActionEvent e,
		@NotNull TriConsumer<DuplicateUtil, Integer, Integer> duplicator
	) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var document = editor.getDocument();
		
		// noinspection ConstantConditions
		final var duplicateUtil = new DuplicateUtil(project, editor, document);
		for (Caret caret : editor.getCaretModel().getAllCarets()) {
			duplicator.accept(
				duplicateUtil,
				caret.getSelectionStart(),
				caret.getSelectionEnd()
			);
		}
	}
}
