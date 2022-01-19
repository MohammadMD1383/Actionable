package ir.mmd.intellijDev.Actionable.caret.justification;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class JustifyCaretsStart extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		
		//noinspection ConstantConditions
		new JustifyCaretUtil(project, editor).justifyCaretsStart();
	}
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final Project project = e.getProject();
		final Editor editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(
			project != null && editor != null &&
				editor.getCaretModel().getCaretCount() > 1 &&
				!editor.getSelectionModel().hasSelection(true)
		);
	}
}
