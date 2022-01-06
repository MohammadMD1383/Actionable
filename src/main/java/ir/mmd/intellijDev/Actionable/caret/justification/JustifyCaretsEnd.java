package ir.mmd.intellijDev.Actionable.caret.justification;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import ir.mmd.intellijDev.Actionable.action.ActionHelper;
import ir.mmd.intellijDev.Actionable.action.ActionHelper.Actions;
import org.jetbrains.annotations.NotNull;

public class JustifyCaretsEnd extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		
		//noinspection ConstantConditions
		final var caretUtil = new JCaretUtil(project, editor);
		
		caretUtil.backupCarets();
		ActionHelper.setToggleAction(e, Actions.ColumnSelectionMode, true);
		caretUtil.restoreCarets();
		
		caretUtil.justifyCaretsEnd();
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
