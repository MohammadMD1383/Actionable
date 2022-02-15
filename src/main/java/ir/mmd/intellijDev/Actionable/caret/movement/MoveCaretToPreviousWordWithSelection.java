package ir.mmd.intellijDev.Actionable.caret.movement;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.movement.Actions.moveCaretWithSelection;
import static ir.mmd.intellijDev.Actionable.caret.movement.Actions.setActionAvailability;
import static ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.BACKWARD;

public class MoveCaretToPreviousWordWithSelection extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { moveCaretWithSelection(e, BACKWARD); }
	
	@Override
	public void update(@NotNull AnActionEvent e) { setActionAvailability(e); }
}
