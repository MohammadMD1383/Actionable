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
 *     <li>{@link JustifyCaretsEnd}</li>
 *     <li>{@link JustifyCaretsStart}</li>
 * </ul>
 */
public class Actions {
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
