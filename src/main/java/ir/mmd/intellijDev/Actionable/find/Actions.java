package ir.mmd.intellijDev.Actionable.find;

import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.find.FindResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementUtil;
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.*;
import static ir.mmd.intellijDev.Actionable.util.Utility.first;
import static ir.mmd.intellijDev.Actionable.util.Utility.last;

/**
 * This class implements actions required by:<br>
 * <ul>
 *     <li>{@link SelectNextOccurrence}</li>
 *     <li>{@link SelectPreviousOccurrence}</li>
 * </ul>
 */
public class Actions {
	/**
	 * implementation for:
	 * <ul>
	 *     <li>{@link SelectNextOccurrence}</li>
	 *     <li>{@link SelectPreviousOccurrence}</li>
	 * </ul>
	 *
	 * @param e             event of mentioned action
	 * @param searchForward whether to search for the word forward or backward
	 */
	@SuppressWarnings("ConstantConditions")
	public static void selectOccurrence(
		@NotNull AnActionEvent e,
		boolean searchForward
	) {
		final Project project = e.getProject();
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Document document = editor.getDocument();
		final CaretModel caretModel = editor.getCaretModel();
		final Caret caret = searchForward ? last(caretModel.getAllCarets()) : first(caretModel.getAllCarets());
		final ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState caretMovementSettingsState = ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState.getInstance();
		final boolean isCaseSensitive = SettingsState.getInstance().isCaseSensitive;
		final String wordSeparators = caretMovementSettingsState.wordSeparators;
		final String hardStopCharacters = caretMovementSettingsState.hardStopCharacters;
		
		/*
		  if there is no selection, first we need to make a selection around the word.
		  this is like what intelliJ does.
		*/
		if (!caret.hasSelection()) {
			final CaretMovementUtil cutil = new CaretMovementUtil(document, caret);
			final int[] wordBoundaries = getWordBoundaries(cutil, wordSeparators, hardStopCharacters);
			
			// wordBoundaries[0]: startOffset & wordBoundaries[1]: endOffset
			// startOffset == endOffset : there is no word around caret
			if (wordBoundaries[0] != wordBoundaries[1]) {
				final Character endChar = cutil.peek(0);
				final int addition = !wordSeparators.contains(endChar.toString()) ? +1 : 0;
				caret.setSelection(wordBoundaries[0], wordBoundaries[1] + addition);
			}
			return;
		}
		
		final FindModel findModel = new FindModel();
		findModel.setForward(searchForward);
		findModel.setCaseSensitive(isCaseSensitive);
		findModel.setStringToFind(caret.getSelectedText());
		
		final FindManager findManager = FindManager.getInstance(project);
		final FindResult findResult = findManager.findString(
			document.getCharsSequence(),
			searchForward ? caret.getSelectionEnd() : caret.getSelectionStart(),
			findModel
		);
		
		if (findResult.isStringFound()) {
			final VisualPosition logicalPosition = editor.offsetToVisualPosition(findResult.getStartOffset() + (caret.getOffset() - caret.getSelectionStart()));
			final Caret newCaret = caretModel.addCaret(logicalPosition, true);
			newCaret.setSelection(findResult.getStartOffset(), findResult.getEndOffset());
		}
	}
	
	public static int @NotNull [] getWordBoundaries(
		@NotNull CaretMovementUtil cutil,
		@NotNull String wordSeparators,
		@NotNull String hardStops
	) {
		goUntilReached(cutil, wordSeparators, hardStops, BACKWARD);
		final int startOffset = cutil.getOffset();
		
		/*
		  because `goUntilReached` method will look for the next character relative to the character associated with the caret,
		  not visually the character after caret.
		  visually the character after caret, is actually the character that is associated with the caret.
		*/
		cutil.reset(-1);
		
		goUntilReached(cutil, wordSeparators, hardStops, FORWARD);
		cutil.go(+1); // because of the `-1` at upper statement
		final int endOffset = cutil.getOffset();
		
		return new int[]{startOffset, endOffset};
	}
}
