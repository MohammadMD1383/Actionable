package ir.mmd.intellijDev.Actionable.caret.movement;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.movement.Actions.moveCaretWithSelection;
import static ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.FORWARD;

public class MoveCaretToNextWordWithSelection extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { moveCaretWithSelection(e, FORWARD); }
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final var editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(editor != null);
	}
}
