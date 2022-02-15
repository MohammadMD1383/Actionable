package ir.mmd.intellijDev.Actionable.caret.movement;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.movement.Actions.moveCaret;
import static ir.mmd.intellijDev.Actionable.caret.movement.Actions.setActionAvailability;
import static ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.FORWARD;

public class MoveCaretToNextWord extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { moveCaret(e, FORWARD); }
	
	@Override
	public void update(@NotNull AnActionEvent e) { setActionAvailability(e); }
}
