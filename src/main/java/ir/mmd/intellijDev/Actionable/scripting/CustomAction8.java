package ir.mmd.intellijDev.Actionable.scripting;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import ir.mmd.intellijDev.Actionable.scripting.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.scripting.Actions.mimActionPerformed;

public class CustomAction8 extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { mimActionPerformed(e, SettingsState.getInstance().customAction8, 8); }
}
