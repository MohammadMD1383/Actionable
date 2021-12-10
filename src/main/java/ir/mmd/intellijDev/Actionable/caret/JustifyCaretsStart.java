package ir.mmd.intellijDev.Actionable.caret;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import org.jetbrains.annotations.NotNull;

public class JustifyCaretsStart extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		
		//noinspection ConstantConditions
		new CaretUtil(project, editor).justifyCaretsStart();
	}
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabledAndVisible(
			project != null && editor != null &&
				editor.getCaretModel().getCaretCount() > 1 &&
				!editor.getSelectionModel().hasSelection(true)
		);
	}
}
