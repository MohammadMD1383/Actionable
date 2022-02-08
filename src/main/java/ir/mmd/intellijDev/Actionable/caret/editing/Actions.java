package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementUtil;
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static ir.mmd.intellijDev.Actionable.find.Actions.getWordBoundaries;
import static ir.mmd.intellijDev.Actionable.util.Utility.copyToClipboard;

public class Actions {
	public static final Key<String> scheduledPasteActionKind = new Key<>("scheduledPasteAction.kink");
	public static final Key<Integer> scheduledPasteActionOffset = new Key<>("scheduledPasteAction.offset");
	
	public static void copyElementAtCaret(
		@NotNull AnActionEvent e,
		boolean deleteElement
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Document document = editor.getDocument();
		final Caret care = editor.getCaretModel().getPrimaryCaret();
		final PsiElement element = getElementAtCaret(project, document, care);
		
		copyToClipboard(element.getText());
		if (deleteElement) runWriteCommandAction(project, element::delete);
	}
	
	public static void copyWordAtCaret(
		@NotNull AnActionEvent e,
		boolean deleteWord
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Document document = editor.getDocument();
		final Caret caret = editor.getCaretModel().getPrimaryCaret();
		final int[] wordBoundaries = new int[2];
		final String word = getWordAtCaret(document, caret, wordBoundaries);
		
		if (word != null) {
			copyToClipboard(word);
			if (deleteWord) runWriteCommandAction(project, () -> document.deleteString(wordBoundaries[0], wordBoundaries[1]));
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	public static void setPasteOffset(
		@NotNull AnActionEvent e,
		@NotNull String actionName
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Caret caret = editor.getCaretModel().getPrimaryCaret();
		final String kind = project.getUserData(scheduledPasteActionKind);
		final Integer offset = project.getUserData(scheduledPasteActionOffset);
		
		// this also ensures not null
		if (actionName.equals(kind)) {
			if (offset == caret.getOffset()) {
				project.putUserData(scheduledPasteActionKind, null);
				project.putUserData(scheduledPasteActionOffset, null);
			} else /* offset != caret.getOffset() */ {
				project.putUserData(scheduledPasteActionOffset, caret.getOffset());
			}
			return;
		}
		
		project.putUserData(scheduledPasteActionKind, actionName);
		project.putUserData(scheduledPasteActionOffset, caret.getOffset());
	}
	
	@SuppressWarnings("ConstantConditions")
	public static PsiElement getElementAtCaret(
		Project project,
		@NotNull Document document,
		@NotNull Caret caret
	) {
		final VirtualFile file = FileDocumentManager.getInstance().getFile(document);
		final int caretOffset = caret.getOffset();
		return PsiManager.getInstance(project).findFile(file).findElementAt(caretOffset);
	}
	
	@SuppressWarnings("ConstantConditions")
	public static @Nullable String getWordAtCaret(
		@NotNull Document document,
		@NotNull Caret caret,
		int @Nullable [] wb
	) {
		final CharSequence documentChars = document.getCharsSequence();
		final SettingsState settingsState = SettingsState.getInstance();
		final String wordSeparators = settingsState.wordSeparators;
		final String hardStopCharacters = settingsState.hardStopCharacters;
		final CaretMovementUtil cutil = new CaretMovementUtil(document, caret);
		final int[] wordBoundaries = getWordBoundaries(cutil, wordSeparators, hardStopCharacters);
		
		if (wordBoundaries[0] != wordBoundaries[1]) {
			if (wb != null) {
				wb[0] = wordBoundaries[0];
				wb[1] = wordBoundaries[1];
			}
			
			final int addition = !wordSeparators.contains(cutil.peek(0).toString()) ? +1 : 0;
			return documentChars.subSequence(wordBoundaries[0], wordBoundaries[1] + addition).toString();
		} else return null;
	}
}
