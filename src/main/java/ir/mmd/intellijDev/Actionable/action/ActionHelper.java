package ir.mmd.intellijDev.Actionable.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;

/**
 * this class is used to interact with other actions e.g. IDE actions
 */
public class ActionHelper {
	
	/**
	 * this class holds needed action ids
	 */
	public static class Actions {
		public static final String ColumnSelectionMode = "EditorToggleColumnMode";
	}
	
	/**
	 * sets a {@link ToggleAction}'s state
	 *
	 * @param event event of execution
	 * @param action the action to be manipulated
	 * @param enabled whether to enable the state or disable it
	 */
	public static void setToggleAction(
		@NotNull AnActionEvent event,
		@NotNull String action,
		boolean enabled
	) {
		((ToggleAction) ActionManager.getInstance().getAction(action)).setSelected(event, enabled);
	}
}
