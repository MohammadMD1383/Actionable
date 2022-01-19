package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Actions {
	@SuppressWarnings("ConstantConditions")
	public static void copyElementAtCaret(
		@NotNull AnActionEvent e,
		boolean deleteElement
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
		
		final int caretOffset = editor.getCaretModel().getOffset();
		final PsiElement wordElement = PsiManager.getInstance(project).findFile(file).findElementAt(caretOffset);
		final String word = wordElement.getText();
		
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(word), null);
		
		if (deleteElement) WriteCommandAction.runWriteCommandAction(project, wordElement::delete);
	}
}
