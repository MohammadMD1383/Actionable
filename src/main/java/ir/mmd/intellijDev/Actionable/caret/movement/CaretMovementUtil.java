package ir.mmd.intellijDev.Actionable.caret.movement;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is a wrapper class over {@link Caret} for convenient movement
 */
class CaretMovementUtil {
	private final @NotNull Document document;
	private final @NotNull Caret caret;
	
	/**
	 * temporary caret offset
	 */
	private int caretOffset;
	
	public CaretMovementUtil(
		@NotNull Document document,
		@NotNull Caret caret
	) {
		this.document = document;
		this.caret = caret;
		
		caretOffset = caret.getOffset();
	}
	
	/**
	 * changes back the {@link CaretMovementUtil#caretOffset} to the <b>last {@link CaretMovementUtil#commit(int)}</b>
	 */
	public void reset() { if (caret.isValid()) caretOffset = caret.getOffset(); }
	
	public int getOffset() { return caretOffset; }
	
	/**
	 * peeks and returns the character at the given offset from current {@link CaretMovementUtil#caretOffset}
	 *
	 * @param offset the offset from current <b>temporary caret position</b>
	 * @return the character or null if the evaluated offset is invalid in the parent {@link Document} of the {@link Caret}
	 */
	public @Nullable Character peek(int offset) {
		final var documentChars = document.getCharsSequence();
		final var newOffset = caretOffset + offset;
		
		if (newOffset >= 0 && newOffset < documentChars.length())
			return documentChars.charAt(newOffset);
		return null;
	}
	
	/**
	 * moves the temporary {@link CaretMovementUtil#caretOffset}
	 *
	 * @param offset can be either positive or negative
	 */
	public void go(int offset) { caretOffset += offset; }
	
	/**
	 * moves the {@link CaretMovementUtil#caret} to the {@link CaretMovementUtil#caretOffset}
	 *
	 * @param offset the offset to be added to {@link CaretMovementUtil#caretOffset} before committing
	 */
	public void commit(int offset) { if (caret.isValid()) caret.moveToOffset(caretOffset += offset); }
}