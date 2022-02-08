package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static ir.mmd.intellijDev.Actionable.caret.editing.Actions.getWordAtCaret;

public class ExecutePasteAction extends AnAction {
	@Override
	@SuppressWarnings("ConstantConditions")
	public void actionPerformed(@NotNull AnActionEvent e) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Document document = editor.getDocument();
		final Caret caret = editor.getCaretModel().getPrimaryCaret();
		final String kind = project.getUserData(Actions.scheduledPasteActionKind);
		final Integer pasteOffset = project.getUserData(Actions.scheduledPasteActionOffset);
		
		if (kind != null) {
			final String[] commands = kind.split(";");
			final boolean isCut = commands[1].equals("ct");
			
			if (commands[0].equals("el")) {
				final PsiElement element = Actions.getElementAtCaret(project, document, caret);
				runWriteCommandAction(project, () -> {
					// final PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
					document.insertString(pasteOffset, element.getText());
					if (isCut) element.delete();
				});
			} else /* commands[0].equals("wd") */ {
				final int[] wb = new int[2];
				final String word = getWordAtCaret(document, caret, wb);
				if (word != null) {
					runWriteCommandAction(project, () -> {
						document.insertString(pasteOffset, word);
						if (isCut) document.deleteString(wb[0], wb[1]);
					});
				}
			}
			// todo: must check the caret offset before removing...
			project.putUserData(Actions.scheduledPasteActionKind, null);
			project.putUserData(Actions.scheduledPasteActionOffset, null);
		}
	}
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final Project project = e.getProject();
		final Editor editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(
			project != null && editor != null &&
				editor.getCaretModel().getCaretCount() == 1
		);
	}
}
