package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
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
				final TextRange elementRange = element.getTextRange();
				runWriteCommandAction(project, () -> paste(
					document,
					elementRange.getStartOffset(),
					elementRange.getEndOffset(),
					pasteOffset,
					isCut
				));
			} else /* commands[0].equals("wd") */ {
				final int[] wb = new int[2];
				final String word = getWordAtCaret(document, caret, wb);
				if (word != null) {
					runWriteCommandAction(project, () -> paste(
						document, wb[0], wb[1], pasteOffset, isCut
					));
				}
			}
			
			project.putUserData(Actions.scheduledPasteActionKind, null);
			project.putUserData(Actions.scheduledPasteActionOffset, null);
		}
	}
	
	/**
	 * will paste the specified range of text into the specified offset
	 * and will take care of <i>cut</i> action
	 *
	 * @param document instance of the {@link Document}
	 * @param start    text start position
	 * @param end      text end position
	 * @param offset   offset to paste at
	 * @param isCut    whether to delete the text after pasting or not
	 */
	private void paste(@NotNull Document document, int start, int end, int offset, boolean isCut) {
		final String text = document.getText(new TextRange(start, end));
		
		/*
		  this is actually for elements, because we have just elements that contain whitespaces.
		  words won't contain whitespaces.
		*/
		if (text.trim().length() == 0) return;
		
		if (offset > start && offset > end) {
			document.insertString(offset, text);
			if (isCut) document.deleteString(start, end);
		} else if (offset < start && offset < end) {
			if (isCut) document.deleteString(start, end);
			document.insertString(offset, text);
		} /* else -> offset is between the start and end: ignore */
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