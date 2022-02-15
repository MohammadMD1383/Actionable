package ir.mmd.intellijDev.Actionable.duplicate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.duplicate.Actions.duplicate;
import static ir.mmd.intellijDev.Actionable.duplicate.Actions.setActionAvailability;

public class DuplicateLinesDown extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { duplicate(e, DuplicateUtil::duplicateDown); }
	
	@Override
	public void update(@NotNull AnActionEvent e) { setActionAvailability(e); }
}
