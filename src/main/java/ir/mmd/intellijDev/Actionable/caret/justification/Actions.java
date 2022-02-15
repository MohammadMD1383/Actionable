package ir.mmd.intellijDev.Actionable.caret.justification;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import ir.mmd.intellijDev.Actionable.util.func.VoidFunction0;
import org.jetbrains.annotations.NotNull;

/**
 * this class holds implementation of these actions:
 * <ul>
 *     <li>{@link JustifyCaretsStart}</li>
 *     <li>{@link JustifyCaretsEnd}</li>
 *     <li>{@link JustifyCaretsEndAndShift}</li>
 * </ul>
 */
public class Actions {
	/**
	 * common availability criteria among these actions:
	 * <ul>
	 *     <li>{@link JustifyCaretsStart}</li>
	 *     <li>{@link JustifyCaretsEnd}</li>
	 *     <li>{@link JustifyCaretsEndAndShift}</li>
	 * </ul>
	 *
	 * @param e event of execution
	 */
	public static void setActionAvailability(@NotNull AnActionEvent e) {
		final Project project = e.getProject();
		final Editor editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(
			project != null && editor != null &&
				editor.getCaretModel().getCaretCount() > 1 &&
				!editor.getSelectionModel().hasSelection(true)
		);
	}
	
	/**
	 * executes the given <code>justifier</code>
	 *
	 * @param e         event of execution
	 * @param justifier a method of {@link JustifyCaretUtil}
	 */
	public static void justifyCarets(
		@NotNull AnActionEvent e,
		@NotNull VoidFunction0<JustifyCaretUtil> justifier
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		
		//noinspection ConstantConditions
		final JustifyCaretUtil caretUtil = new JustifyCaretUtil(project, editor);
		justifier.exec(caretUtil);
	}
}
