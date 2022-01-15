package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class CopyWordAtCaretAction extends AnAction {
	@Override
	@SuppressWarnings("ConstantConditions")
	public void actionPerformed(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var file = FileDocumentManager.getInstance().getFile(editor.getDocument());
		
		final var caretOffset = editor.getCaretModel().getOffset();
		final var word = PsiManager.getInstance(project).findFile(file).findElementAt(caretOffset).getText();
		
		final var clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipBoard.setContents(new StringSelection(word), null);
	}
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final var project = e.getProject();
		final var editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(
			project != null && editor != null &&
				editor.getCaretModel().getCaretCount() == 1
		);
	}
}
