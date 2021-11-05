package ir.mmd.intellijDev.Actionable.duplicate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DuplicateLinesDown extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		
		new DuplicateUtil(project, editor).duplicateDown();
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
