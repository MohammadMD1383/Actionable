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

/**
 * This class contains implementation for these actions:
 * <ul>
 *     <li>{@link CopyElementAtCaret}</li>
 *     <li>{@link CutElementAtCaret}</li>
 *     <li>{@link CopyWordAtCaret}</li>
 *     <li>{@link CutWordAtCaret}</li>
 *     <li>{@link SetElementCopyPasteOffset}</li>
 *     <li>{@link SetElementCutPasteOffset}</li>
 *     <li>{@link SetWordCopyPasteOffset}</li>
 *     <li>{@link SetWordCutPasteOffset}</li>
 * </ul>
 */
public class Actions {
	public static final Key<String> scheduledPasteActionKind = new Key<>("scheduledPasteAction.kink");
	public static final Key<Integer> scheduledPasteActionOffset = new Key<>("scheduledPasteAction.offset");
	
	/**
	 * implementation of:
	 * <ul>
	 *     <li>{@link CopyElementAtCaret}</li>
	 *     <li>{@link CutElementAtCaret}</li>
	 * </ul>
	 *
	 * @param e             instance of {@link AnActionEvent}
	 * @param deleteElement if true, element will be deleted after being copied,
	 *                      in other words it cuts the element
	 */
	public static void copyElementAtCaret(
		@NotNull AnActionEvent e,
		boolean deleteElement
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Document document = editor.getDocument();
		final Caret caret = editor.getCaretModel().getPrimaryCaret();
		final PsiElement element = getElementAtCaret(project, document, caret);
		
		copyToClipboard(element.getText());
		if (deleteElement) runWriteCommandAction(project, element::delete);
	}
	
	/**
	 * implementation of:
	 * <ul>
	 *     <li>{@link CopyWordAtCaret}</li>
	 *     <li>{@link CutWordAtCaret}</li>
	 * </ul>
	 *
	 * @param e          instance of {@link AnActionEvent}
	 * @param deleteWord if true, word will be deleted after being copied,
	 *                   in other words it cuts the word
	 */
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
	
	/**
	 * implementation of:
	 * <ul>
	 *     <li>{@link SetElementCopyPasteOffset}</li>
	 *     <li>{@link SetElementCutPasteOffset}</li>
	 *     <li>{@link SetWordCopyPasteOffset}</li>
	 *     <li>{@link SetWordCutPasteOffset}</li>
	 * </ul>
	 *
	 * @param e          instance of {@link AnActionEvent}
	 * @param actionName the action command; see usages for more info
	 */
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
	
	/**
	 * returns the {@link PsiElement} located at caret
	 *
	 * @param project  instance of current {@link Project}
	 * @param document instance of the {@link Document}
	 * @param caret    the {@link Caret} to find the element at
	 * @return the element located at the specified {@link Caret}
	 */
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
	
	/**
	 * returns the word located at the caret
	 *
	 * @param document instance of the {@link Document}
	 * @param caret    the {@link Caret} to find the word at
	 * @param wb       [optional] array of two elements that will be filled with the word boundaries
	 *                 found at the caret
	 * @return a string containing the word or null if the word is not matched
	 */
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
			/* check if the caller wants the word boundaries */
			if (wb != null) {
				wb[0] = wordBoundaries[0];
				wb[1] = wordBoundaries[1];
			}
			
			final int addition = !wordSeparators.contains(cutil.peek(0).toString()) ? +1 : 0;
			return documentChars.subSequence(wordBoundaries[0], wordBoundaries[1] + addition).toString();
		} else return null;
	}
}
