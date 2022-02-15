package ir.mmd.intellijDev.Actionable.find;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.find.Actions.selectOccurrence;
import static ir.mmd.intellijDev.Actionable.find.Actions.setActionAvailability;

public class SelectPreviousOccurrence extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { selectOccurrence(e, false); }
	
	@Override
	public void update(@NotNull AnActionEvent e) { setActionAvailability(e); }
}
