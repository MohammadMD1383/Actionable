package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.editing.Actions.setPasteOffset;

public class SetElementCopyPasteOffset extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { setPasteOffset(e, "el;cp"); }
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final Project project = e.getProject();
		final Editor editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(
			project != null && editor != null &&
				editor.getCaretModel().getCaretCount() == 1
		);
	}
}
