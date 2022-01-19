package ir.mmd.intellijDev.Actionable.duplicate;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import ir.mmd.intellijDev.Actionable.util.func.VoidFunction2;
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
	 * @param e          event of mentioned actions
	 * @param duplicator a method reference from {@link DuplicateUtil} class
	 */
	public static void duplicate(
		@NotNull AnActionEvent e,
		@NotNull VoidFunction2<DuplicateUtil, Integer, Integer> duplicator
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Document document = editor.getDocument();
		
		// noinspection ConstantConditions
		final DuplicateUtil duplicateUtil = new DuplicateUtil(project, editor, document);
		for (Caret caret : editor.getCaretModel().getAllCarets()) {
			duplicator.exec(
				duplicateUtil,
				caret.getSelectionStart(),
				caret.getSelectionEnd()
			);
		}
	}
}
