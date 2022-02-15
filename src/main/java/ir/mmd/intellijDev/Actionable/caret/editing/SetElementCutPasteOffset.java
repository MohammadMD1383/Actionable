package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.editing.Actions.setActionAvailability;
import static ir.mmd.intellijDev.Actionable.caret.editing.Actions.setPasteOffset;

public class SetElementCutPasteOffset extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { setPasteOffset(e, "el;ct"); }
	
	@Override
	public void update(@NotNull AnActionEvent e) { setActionAvailability(e); }
}
