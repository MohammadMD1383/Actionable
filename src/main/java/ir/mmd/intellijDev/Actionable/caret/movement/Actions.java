package ir.mmd.intellijDev.Actionable.caret.movement;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.BACKWARD;
import static ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementHelper.FORWARD;
import static ir.mmd.intellijDev.Actionable.util.Utility.inRange;

/**
 * This class implements actions required by:<br>
 * <ul>
 *     <li>{@link MoveCaretToNextWord}</li>
 *     <li>{@link MoveCaretToNextWordWithSelection}</li>
 *     <li>{@link MoveCaretToPreviousWord}</li>
 *     <li>{@link MoveCaretToPreviousWordWithSelection}</li>
 * </ul>
 */
public class Actions {
	
	/**
	 * helper method for gathering {@link SettingsState#wordSeparators} and {@link SettingsState#newLineIncluded} together
	 *
	 * @param settingsState see {@link SettingsState#getInstance()}
	 * @return complete list of {@link SettingsState#wordSeparators}
	 */
	private static @NotNull List<Character> getWordSeparators(@NotNull SettingsState settingsState) {
		final var wordSeparators = settingsState.wordSeparators.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		if (settingsState.newLineIncluded) wordSeparators.add('\n');
		return wordSeparators;
	}
	
	/**
	 * implementation for:
	 * <ul>
	 *     <li>{@link MoveCaretToNextWord}</li>
	 *     <li>{@link MoveCaretToPreviousWord}</li>
	 * </ul>
	 *
	 * @param e   event of mentioned actions
	 * @param dir see {@link CaretMovementHelper#moveCaret(CaretMovementUtil, List, int, int)}
	 */
	public static void moveCaret(
		@NotNull AnActionEvent e,
		int dir
	) {
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var document = editor.getDocument();
		final var settingsState = SettingsState.getInstance();
		final var wordSeparators = getWordSeparators(settingsState);
		
		for (Caret caret : editor.getCaretModel().getAllCarets()) {
			final var cutil = new CaretMovementUtil(document, caret);
			CaretMovementHelper.moveCaret(
				cutil,
				wordSeparators,
				settingsState.wordSeparatorsBehaviour,
				dir
			);
			/*
			 (in FORWARD movement direction)
			 because the caret stays at the beginning of the character that is associated to,
			 we move it one more character forward, to place it exactly after the character that we want.
			*/
			cutil.commit(dir == FORWARD ? +1 : 0);
		}
	}
	
	/**
	 * implementation for:
	 * <ul>
	 *     <li>{@link MoveCaretToNextWordWithSelection}</li>
	 *     <li>{@link MoveCaretToPreviousWordWithSelection}</li>
	 * </ul>
	 *
	 * @param e   event of mentioned actions
	 * @param dir see {@link CaretMovementHelper#moveCaret(CaretMovementUtil, List, int, int)}
	 */
	public static void moveCaretWithSelection(
		@NotNull AnActionEvent e,
		int dir
	) {
		final var editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final var document = editor.getDocument();
		final var settingsState = SettingsState.getInstance();
		final var wordSeparators = getWordSeparators(settingsState);
		
		final var carets = editor.getCaretModel().getAllCarets();
		final var selectionStarts = carets.stream().map(Caret::getLeadSelectionOffset).collect(Collectors.toList());
		
		if (dir == BACKWARD) {
			Collections.reverse(carets);
			Collections.reverse(selectionStarts);
		}
		
		for (int i = 0; i < carets.size(); i++) {
			final var caret = carets.get(i);
			final var cutil = new CaretMovementUtil(document, caret);
			
			CaretMovementHelper.moveCaret(
				cutil,
				wordSeparators,
				settingsState.wordSeparatorsBehaviour,
				dir
			);
			
			if (i + 1 < carets.size()) {
				final var nextCaret = carets.get(i + 1);
				
				/* `inRange` method will automatically sort the `start` and `end` parameters if they're not in true order */
				if (inRange(cutil.getOffset(), nextCaret.getSelectionStart(), nextCaret.getSelectionEnd())) {
					nextCaret.setSelection(selectionStarts.get(i), nextCaret.getOffset());
					selectionStarts.set(i + 1, selectionStarts.get(i));
					continue;
				}
			}
			
			/* see the note of the same expression at `moveCarets` method */
			cutil.commit(dir == FORWARD ? +1 : 0);
			caret.setSelection(selectionStarts.get(i), caret.getOffset());
		}
	}
}
