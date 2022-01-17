package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class Actions {
	@SuppressWarnings("ConstantConditions")
	public static void copyElementAtCaret(
		@NotNull AnActionEvent e,
		boolean deleteElement
	) {
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var file = FileDocumentManager.getInstance().getFile(editor.getDocument());
		
		final var caretOffset = editor.getCaretModel().getOffset();
		final var wordElement = PsiManager.getInstance(project).findFile(file).findElementAt(caretOffset);
		final var word = wordElement.getText();
		
		final var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(word), null);
		
		if (deleteElement) WriteCommandAction.runWriteCommandAction(project, wordElement::delete);
	}
}
