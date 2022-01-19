package ir.mmd.intellijDev.Actionable.caret.movement;

import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

/**
 * This is a helper class for moving the caret in the editor without committing
 */
public class CaretMovementHelper {
	
	/**
	 * move caret forward
	 */
	public static final int FORWARD = 1;
	
	/**
	 * move caret backward
	 */
	public static final int BACKWARD = -1;
	
	/**
	 * moves the caret until it reaches one the given characters
	 *
	 * @param cutil instance of {@link CaretMovementUtil}
	 * @param chars the characters to stop at
	 * @param dir   either {@link CaretMovementHelper#FORWARD} or {@link CaretMovementHelper#BACKWARD}
	 */
	public static void goUntilReached(
		@NotNull CaretMovementUtil cutil,
		@NotNull String chars,
		int dir
	) {
		while (true) {
			final Character nextChar = cutil.peek(dir);
			if (nextChar == null || chars.contains(nextChar.toString())) break;
			cutil.go(dir);
		}
	}
	
	/**
	 * moves the caret while it faces with one the given characters
	 *
	 * @param cutil instance of {@link CaretMovementUtil}
	 * @param chars the characters to face with
	 * @param dir   either {@link CaretMovementHelper#FORWARD} or {@link CaretMovementHelper#BACKWARD}
	 */
	public static void goWhileHaving(
		@NotNull CaretMovementUtil cutil,
		@NotNull String chars,
		int dir
	) {
		while (true) {
			final Character nextChar = cutil.peek(dir);
			if (nextChar == null || !chars.contains(nextChar.toString())) break;
			cutil.go(dir);
		}
	}
	
	/**
	 * helper method for moving the caret based on the {@link SettingsState#wordSeparatorsBehaviour}
	 *
	 * @param cutil instance of {@link CaretMovementUtil}
	 * @param chars {@link SettingsState#wordSeparators}
	 * @param mode  see {@link SettingsState.WSBehaviour}
	 * @param dir   either {@link CaretMovementHelper#FORWARD} or {@link CaretMovementHelper#BACKWARD}
	 */
	public static void moveCaret(
		@NotNull CaretMovementUtil cutil,
		@NotNull String chars,
		int mode,
		int dir
	) {
		final Character startingChar = cutil.peek(dir == FORWARD ? 0 : -1);
		if (startingChar == null) return;
		
		/*
		  because when the starting character is located at -1
		  so starting point should change to -1.
		*/
		if (dir == BACKWARD) cutil.go(-1);
		
		if (mode == SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE) {
			if (chars.contains(startingChar.toString())) {
				goWhileHaving(cutil, chars, dir);
			} else /* !chars.contains(startingChar) */ {
				goUntilReached(cutil, chars, dir);
			}
		} else /* mode == SettingsState.WSBehaviour.STOP_AT_NEXT_SAME_CHAR_TYPE */ {
			if (chars.contains(startingChar.toString())) {
				goWhileHaving(cutil, chars, dir);
				goUntilReached(cutil, chars, dir);
			} else /* !chars.contains(startingChar) */ {
				goUntilReached(cutil, chars, dir);
				goWhileHaving(cutil, chars, dir);
			}
		}
	}
}
