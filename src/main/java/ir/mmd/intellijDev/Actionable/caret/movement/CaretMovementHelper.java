package ir.mmd.intellijDev.Actionable.caret.movement;

import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CaretMovementHelper {
	public static final int FORWARD = 1;
	public static final int BACKWARD = -1;
	
	public static void goUntilReached(
		@NotNull CaretMovementUtil cutil,
		@NotNull List<Character> chars,
		int dir
	) {
		while (true) {
			final var nextChar = cutil.peek(dir);
			if (nextChar == null || chars.contains(nextChar)) break;
			cutil.go(dir);
		}
	}
	
	public static void goWhileHaving(
		@NotNull CaretMovementUtil cutil,
		@NotNull List<Character> chars,
		int dir
	) {
		while (true) {
			final var nextChar = cutil.peek(dir);
			if (nextChar == null || !chars.contains(nextChar)) break;
			cutil.go(dir);
		}
	}
	
	public static void moveCaret(
		@NotNull CaretMovementUtil cutil,
		@NotNull List<Character> chars,
		int mode,
		int dir
	) {
		Character startingChar;
		startingChar = cutil.peek(dir == FORWARD ? 0 : -1);
		
		if (dir == BACKWARD) cutil.go(-1);
		
		if (mode == SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE) {
			if (chars.contains(startingChar)) {
				goWhileHaving(cutil, chars, dir);
			} else /* !chars.contains(startingChar) */ {
				goUntilReached(cutil, chars, dir);
			}
		} else /* mode == SettingsState.WSBehaviour.STOP_AT_NEXT_SAME_CHAR_TYPE */ {
			if (chars.contains(startingChar)) {
				goWhileHaving(cutil, chars, dir);
				goUntilReached(cutil, chars, dir);
			} else /* !chars.contains(startingChar) */ {
				goUntilReached(cutil, chars, dir);
				goWhileHaving(cutil, chars, dir);
			}
		}
	}
}
