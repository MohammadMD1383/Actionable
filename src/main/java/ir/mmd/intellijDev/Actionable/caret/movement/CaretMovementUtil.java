package ir.mmd.intellijDev.Actionable.caret.movement;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CaretMovementUtil {
	private final @NotNull Document document;
	private final @NotNull Caret caret;
	
	private int caretOffset;
	
	public CaretMovementUtil(
		@NotNull Document document,
		@NotNull Caret caret
	) {
		this.document = document;
		this.caret = caret;
		
		caretOffset = caret.getOffset();
	}
	
	public void reset() { caretOffset = caret.getOffset(); }
	
	public int getOffset() { return caretOffset; }
	
	public @Nullable Character peek(int offset) {
		final var documentChars = document.getCharsSequence();
		final var newOffset = caretOffset + offset;
		
		if (newOffset >= 0 && newOffset < documentChars.length())
			return documentChars.charAt(newOffset);
		return null;
	}
	
	public void go(int offset) { caretOffset += offset; }
	
	public void commit(int offset) { if (caret.isValid()) caret.moveToOffset(caretOffset += offset); }
}
