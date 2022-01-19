package ir.mmd.intellijDev.Actionable.find;

import com.intellij.find.FindManager;
import com.intellij.find.FindModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementUtil;
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
		final var project = e.getProject();
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var document = editor.getDocument();
		final var findSettingsState = ir.mmd.intellijDev.Actionable.find.settings.SettingsState.getInstance();
		final var caretMovementSettingsState = ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState.getInstance();
		final var caretModel = editor.getCaretModel();
		final var caret = searchForward ? last(caretModel.getAllCarets()) : first(caretModel.getAllCarets());
		
		/*
		  if there is no selection, first we need to make a selection around the word.
		  this is like what intelliJ does.
		*/
		if (!caret.hasSelection()) {
			final var cutil = new CaretMovementUtil(document, caret);
			final var wordSeparators = caretMovementSettingsState.wordSeparators;
			final int startOffset;
			final int endOffset;
			
			goUntilReached(cutil, wordSeparators, BACKWARD);
			startOffset = cutil.getOffset();
			
			/*
			  because `goUntilReached` method will look for the next character relative to the character associated with the caret,
			  not visually the character after caret.
			  visually the character after caret, is actually the character that is associated with the caret.
			*/
			cutil.reset(-1);
			
			goUntilReached(cutil, wordSeparators, FORWARD);
			cutil.go(+1); // because of the `-1` at upper statement
			endOffset = cutil.getOffset();
			
			// startOffset == endOffset : there is no word around caret
			if (startOffset != endOffset) {
				final var endChar = cutil.peek(0);
				final var addition = !wordSeparators.contains(endChar.toString()) ? +1 : 0;
				caret.setSelection(startOffset, endOffset + addition);
			}
			return;
		}
		
		final var findModel = new FindModel();
		findModel.setForward(searchForward);
		findModel.setCaseSensitive(findSettingsState.isCaseSensitive);
		findModel.setStringToFind(caret.getSelectedText());
		
		final var findManager = FindManager.getInstance(project);
		final var findResult = findManager.findString(
			document.getCharsSequence(),
			searchForward ? caret.getSelectionEnd() : caret.getSelectionStart(),
			findModel
		);
		
		if (findResult.isStringFound()) {
			final var logicalPosition = editor.offsetToLogicalPosition(findResult.getStartOffset() + (caret.getOffset() - caret.getSelectionStart()));
			final var newCaret = caretModel.addCaret(logicalPosition, true);
			newCaret.setSelection(findResult.getStartOffset(), findResult.getEndOffset());
		}
	}
}
