package ir.mmd.intellijDev.Actionable.find;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.find.Actions.selectOccurrence;

public class SelectNextOccurrence extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { selectOccurrence(e, true); }
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(
			project != null && editor != null
		);
	}
}
