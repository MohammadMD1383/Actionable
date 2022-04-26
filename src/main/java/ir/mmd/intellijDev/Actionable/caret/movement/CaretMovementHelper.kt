package ir.mmd.intellijDev.Actionable.caret.movement

import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState

/**
 * This is a helper class for moving the caret in the editor without committing
 */
object CaretMovementHelper {
	/**
	 * move caret forward
	 */
	const val FORWARD = 1
	
	/**
	 * move caret backward
	 */
	const val BACKWARD = -1
	
	/**
	 * moves the caret until it reaches one the given characters
	 *
	 * @param cutil     instance of [CaretMovementUtil]
	 * @param chars     the characters to stop at
	 * @param hardStops characters to stop at, no matter where they are
	 * @param dir       either [CaretMovementHelper.FORWARD] or [CaretMovementHelper.BACKWARD]
	 */
	fun goUntilReached(
		cutil: CaretMovementUtil,
		chars: String,
		hardStops: String,
		dir: Int
	) {
		while (true) {
			val nextChar = cutil.peek(dir)
			if (nextChar == null || chars.contains(nextChar.toString()) || hardStops.contains(nextChar.toString())) break
			cutil.go(dir)
		}
	}
	
	/**
	 * moves the caret while it faces with one the given characters
	 *
	 * @param cutil     instance of [CaretMovementUtil]
	 * @param chars     the characters to face with
	 * @param hardStops characters to stop at, no matter where they are
	 * @param dir       either [CaretMovementHelper.FORWARD] or [CaretMovementHelper.BACKWARD]
	 */
	private fun goWhileHaving(
		cutil: CaretMovementUtil,
		chars: String,
		hardStops: String,
		dir: Int
	) {
		while (true) {
			val nextChar = cutil.peek(dir)
			if (nextChar == null || !chars.contains(nextChar.toString()) || hardStops.contains(nextChar.toString())) break
			cutil.go(dir)
		}
	}
	
	/**
	 * helper method for moving the caret based on the [SettingsState.wordSeparatorsBehaviour]
	 *
	 * @param cutil     instance of [CaretMovementUtil]
	 * @param chars     [SettingsState.wordSeparators]
	 * @param hardStops characters to stop at, no matter where they are
	 * @param mode      see [SettingsState.WSBehaviour]
	 * @param dir       either [CaretMovementHelper.FORWARD] or [CaretMovementHelper.BACKWARD]
	 */
	@JvmStatic
	fun moveCaret(
		cutil: CaretMovementUtil,
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
			if (dir == BACKWARD) cutil.go(-1)
			return
		}
		
		/*
		  because when the starting character is located at -1
		  so starting point should change to -1.
		*/
		if (dir == BACKWARD) cutil.go(-1)
		
		if (mode == SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE) {
			if (startingChar in chars) {
				goWhileHaving(cutil, chars, hardStops, dir)
			} else /* startingChar !in chars */ {
				goUntilReached(cutil, chars, hardStops, dir)
			}
		} else /* mode == SettingsState.WSBehaviour.STOP_AT_NEXT_SAME_CHAR_TYPE */ {
			if (startingChar in chars) {
				goWhileHaving(cutil, chars, hardStops, dir)
				goUntilReached(cutil, chars, hardStops, dir)
			} else /* startingChar !in chars */ {
				goUntilReached(cutil, chars, hardStops, dir)
				goWhileHaving(cutil, chars, hardStops, dir)
			}
		}
	}
}
