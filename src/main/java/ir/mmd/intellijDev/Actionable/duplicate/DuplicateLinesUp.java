package ir.mmd.intellijDev.Actionable.duplicate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import org.jetbrains.annotations.NotNull;

/**
 * AnAction that will duplicate selected line(s) up like how <b>VSCode</b> does
 */
public class DuplicateLinesUp extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var document = editor.getDocument();
		
		// noinspection ConstantConditions
		final var duplicateUtil = new DuplicateUtil(project, editor, document);
		
		for (Caret caret : editor.getCaretModel().getAllCarets()) {
			duplicateUtil.duplicateUp(
				caret.getSelectionStart(),
				caret.getSelectionEnd()
			);
		}
	}
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabledAndVisible(
			project != null && editor != null
		);
	}
}
