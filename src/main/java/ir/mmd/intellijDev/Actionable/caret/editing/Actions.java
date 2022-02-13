package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementUtil;
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction;
import static ir.mmd.intellijDev.Actionable.caret.movement.OffsetMovementUtil.getCharAtOffset;
import static ir.mmd.intellijDev.Actionable.find.Actions.getWordBoundaries;
import static ir.mmd.intellijDev.Actionable.util.Utility.*;

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
	public static final Key<RangeHighlighter> previousHighlighterKey = new Key<>("scheduledPasteAction.motionListener");
	
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
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Caret caret = editor.getCaretModel().getPrimaryCaret();
		final boolean showPasteActionHints = ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState.getInstance().showPasteActionHints;
		final String kind = editor.getUserData(scheduledPasteActionKind);
		final Integer offset = editor.getUserData(scheduledPasteActionOffset);
		
		// this also ensures not null
		if (actionName.equals(kind)) {
			if (offset == caret.getOffset()) {
				removeScheduledPasteAction(editor);
			} else /* offset != caret.getOffset() */ {
				editor.putUserData(scheduledPasteActionOffset, caret.getOffset());
			}
			return;
		}
		
		editor.putUserData(scheduledPasteActionKind, actionName);
		editor.putUserData(scheduledPasteActionOffset, caret.getOffset());
		if (showPasteActionHints) editor.addEditorMouseMotionListener(motionListener);
	}
	
	/**
	 * removes the <code>scheduledPasteAction</code> from the given editor
	 *
	 * @param editor the editor to remove paste action from
	 */
	public static void removeScheduledPasteAction(@NotNull Editor editor) {
		if (ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState.getInstance().showPasteActionHints) {
			editor.removeEditorMouseMotionListener(motionListener);
			final RangeHighlighter rangeHighlighter = editor.getUserData(previousHighlighterKey);
			if (rangeHighlighter != null) editor.getMarkupModel().removeHighlighter(rangeHighlighter);
			editor.putUserData(previousHighlighterKey, null);
		}
		
		editor.putUserData(scheduledPasteActionKind, null);
		editor.putUserData(scheduledPasteActionOffset, null);
	}
	
	/**
	 * this is used to highlight the word/element which is under the caret when there is an active <code>scheduledPasteAction</code>
	 */
	public static final EditorMouseMotionListener motionListener = new EditorMouseMotionListener() {
		@Override
		@SuppressWarnings("ConstantConditions")
		public void mouseMoved(@NotNull EditorMouseEvent e) {
			final Editor editor = e.getEditor();
			final RangeHighlighter previousHighlighter = editor.getUserData(previousHighlighterKey);
			final int offset = editor.logicalPositionToOffset(editor.xyToLogicalPosition(e.getMouseEvent().getPoint()));
			
			if (previousHighlighter == null || !inRange(offset, previousHighlighter.getStartOffset(), previousHighlighter.getEndOffset())) {
				final Project project = editor.getProject();
				final Document document = editor.getDocument();
				final MarkupModel markupModel = editor.getMarkupModel();
				final String pasteKind = editor.getUserData(scheduledPasteActionKind);
				final boolean isElementTarget = pasteKind.split(";")[0].equals("el");
				final int startOffset, endOffset;
				
				if (isElementTarget) {
					final PsiElement element = getElementAtOffset(project, document, offset);
					if (element == null || element.getText().trim().equals("")) {
						startOffset = 0;
						endOffset = 0;
					} else {
						final TextRange elementRange = element.getTextRange();
						startOffset = elementRange.getStartOffset();
						endOffset = elementRange.getEndOffset();
					}
				} else /* isWordTarget */ {
					final int[] wordBoundaries = new int[2];
					getWordAtOffset(document, offset, wordBoundaries);
					startOffset = wordBoundaries[0];
					endOffset = wordBoundaries[1];
				}
				
				if (previousHighlighter != null) markupModel.removeHighlighter(previousHighlighter);
				editor.putUserData(previousHighlighterKey, markupModel.addRangeHighlighter(
					startOffset,
					endOffset,
					HighlighterLayer.LAST + 10,
					EditorColors.IDENTIFIER_UNDER_CARET_ATTRIBUTES.getDefaultAttributes(),
					HighlighterTargetArea.EXACT_RANGE
				));
			}
		}
		
		/**
		 * implemented to be compatible with earlier versions of the intellij idea
		 */
		@Override
		public void mouseDragged(@NotNull EditorMouseEvent e) { }
	};
	
	/**
	 * returns the {@link PsiElement} at the given offset in the given {@link Document}
	 *
	 * @param project  instance of {@link Project}
	 * @param document instance of {@link Document}
	 * @param offset   offset in the document to find element at
	 * @return the element
	 */
	@SuppressWarnings("ConstantConditions")
	public static @Nullable PsiElement getElementAtOffset(
		@NotNull Project project,
		@NotNull Document document,
		int offset
	) {
		final VirtualFile file = FileDocumentManager.getInstance().getFile(document);
		return PsiManager.getInstance(project).findFile(file).findElementAt(offset);
	}
	
	/**
	 * returns the {@link PsiElement} located at caret
	 *
	 * @param project  instance of current {@link Project}
	 * @param document instance of the {@link Document}
	 * @param caret    the {@link Caret} to find the element at
	 * @return the element located at the specified {@link Caret}
	 */
	public static PsiElement getElementAtCaret(
		Project project,
		@NotNull Document document,
		@NotNull Caret caret
	) { return getElementAtOffset(project, document, caret.getOffset()); }
	
	/**
	 * returns the word at the specified offset in the given document
	 *
	 * @param document instance of {@link Document}
	 * @param offset   offset in the document to find word at
	 * @param wb       [optional] an int[2] to revive word boundaries
	 * @return the word
	 */
	@SuppressWarnings("ConstantConditions")
	public static @Nullable String getWordAtOffset(
		@NotNull Document document,
		int offset,
		int @Nullable [] wb
	) {
		final CharSequence documentChars = document.getCharsSequence();
		final SettingsState settingsState = SettingsState.getInstance();
		final String wordSeparators = settingsState.wordSeparators;
		final String hardStopCharacters = settingsState.hardStopCharacters;
		final int[] wordBoundaries = getWordBoundaries(document, wordSeparators, hardStopCharacters, offset);
		
		if (wordBoundaries[0] != wordBoundaries[1]) {
			/* check if the caller wants the word boundaries */
			if (wb != null) {
				wb[0] = wordBoundaries[0];
				wb[1] = wordBoundaries[1];
			}
			
			final int addition = !isInCollection(getCharAtOffset(document, offset), wordSeparators, hardStopCharacters) ? +1 : 0;
			return documentChars.subSequence(wordBoundaries[0], wordBoundaries[1] + addition).toString();
		} else return null;
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
			
			final int addition = !isInCollection(cutil.peek(0), wordSeparators, hardStopCharacters) ? +1 : 0;
			return documentChars.subSequence(wordBoundaries[0], wordBoundaries[1] + addition).toString();
		} else return null;
	}
}
