package ir.mmd.intellijDev.Actionable.caret.justification;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.justification.Actions.justifyCarets;
import static ir.mmd.intellijDev.Actionable.caret.justification.Actions.setActionAvailability;

public class JustifyCaretsEndAndShift extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { justifyCarets(e, JustifyCaretUtil::justifyCaretsEndWithShifting); }
	
	@Override
	public void update(@NotNull AnActionEvent e) { setActionAvailability(e); }
}
