package ir.mmd.intellijDev.Actionable.caret.movement;

import com.intellij.openapi.editor.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ir.mmd.intellijDev.Actionable.util.Utility.safeGet;

public class OffsetMovementUtil {
	/**
	 * returns the character at the given offset in the specified document
	 *
	 * @param document instance of {@link Document}
	 * @param offset   offset in the document
	 * @return the character or null if the offset is out of bounds
	 */
	public static @Nullable Character getCharAtOffset(
		@NotNull Document document,
		int offset
	) { return safeGet(document.getCharsSequence(), offset); }
	
	/**
	 * moves in the document until it reaches one the given characters
	 *
	 * @param document  instance of {@link Document}
	 * @param chars     the characters to stop at
	 * @param hardStops characters to stop at, no matter where they are
	 * @param offset    the offset to start at
	 * @param dir       either {@link CaretMovementHelper#FORWARD} or {@link CaretMovementHelper#BACKWARD}
	 * @return the final offset after moving
	 */
	public static int goUntilReached(
		@NotNull Document document,
		@NotNull String chars,
		@NotNull String hardStops,
		int offset,
		int dir
	) {
		int newOffset = offset;
		while (true) {
			final Character nextChar = getCharAtOffset(document, newOffset + dir);
			if (nextChar == null || chars.contains(nextChar.toString()) || hardStops.contains(nextChar.toString())) break;
			newOffset += dir;
		}
		return newOffset;
	}

// --Commented out by Inspection START (2/14/22, 1:40 PM):
//	/**
//	 * moves in the document while it faces with one the given characters
//	 *
//	 * @param document  instance of {@link Document}
//	 * @param chars     the characters to face with
//	 * @param hardStops characters to stop at, no matter where they are
//	 * @param offset    the offset to start at
//	 * @param dir       either {@link CaretMovementHelper#FORWARD} or {@link CaretMovementHelper#BACKWARD}
//	 * @return the final offset after moving
//	 */
//	public static int goWhileHaving(
//		@NotNull Document document,
//		@NotNull String chars,
//		@NotNull String hardStops,
//		int offset,
//		int dir
//	) {
//		int newOffset = offset;
//		while (true) {
//			final Character nextChar = getCharAtOffset(document, newOffset + dir);
//			if (nextChar == null || !chars.contains(nextChar.toString()) || hardStops.contains(nextChar.toString())) break;
//			newOffset += dir;
//		}
//		return newOffset;
//	}
// --Commented out by Inspection STOP (2/14/22, 1:40 PM)
}
