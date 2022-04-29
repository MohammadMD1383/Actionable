package ir.mmd.intellijDev.Actionable.caret.movement

import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.BACKWARD
import ir.mmd.intellijDev.Actionable.caret.movement.CaretUtil.Companion.FORWARD
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState

/**
 * This is a helper class for moving the caret in the editor without committing
 */
object CaretMovementHelper {

	
	/**
	 * helper method for moving the caret based on the [SettingsState.wordSeparatorsBehaviour]
	 *
	 * @param cutil     instance of [CaretUtil]
	 * @param chars     [SettingsState.wordSeparators]
	 * @param hardStops characters to stop at, no matter where they are
	 * @param mode      see [SettingsState.WSBehaviour]
	 * @param dir       either [CaretMovementHelper.FORWARD] or [CaretMovementHelper.BACKWARD]
	 */
	fun moveCaret(
		cutil: CaretUtil,
		chars: String,
		hardStops: String,
		mode: Int,
		dir: Int
	) {
		/*
		  starting char is actually the next char, where is the starting place of moving.
		  caret must move at least one character, if it hasn't reached the start/end of the document yet.
		*/
		val startingChar = cutil.peek(if (dir == FORWARD) 0 else -1) ?: return
		
		/*
		  hard stop if caret reached one of hard stop characters
		*/
		if (startingChar in hardStops) {
			if (dir == BACKWARD) cutil.move(-1)
			return
		}
		
		/*
		  because when the starting character is located at -1
		  so starting point should change to -1.
		*/
		if (dir == BACKWARD) cutil.move(-1)
		
		if (mode == SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE) {
			if (startingChar in chars)
				cutil.moveWhileFacing(chars, hardStops, dir)
			else /* startingChar !in chars */
				cutil.moveUntilReach(chars, hardStops, dir)
		} else /* mode == SettingsState.WSBehaviour.STOP_AT_NEXT_SAME_CHAR_TYPE */ {
			if (startingChar in chars) {
				cutil.moveWhileFacing(chars, hardStops, dir)
				cutil.moveUntilReach(chars, hardStops, dir)
			} else /* startingChar !in chars */ {
				cutil.moveUntilReach(chars, hardStops, dir)
				cutil.moveWhileFacing(chars, hardStops, dir)
			}
		}
	}
}
