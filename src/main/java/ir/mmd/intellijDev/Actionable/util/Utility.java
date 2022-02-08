package ir.mmd.intellijDev.Actionable.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.List;

/**
 * This class contains utility methods for java sources
 */
public class Utility {
	/**
	 * checks whether the given integer is in range of other two parameters<br>
	 * automatically changes <code>start</code> and <code>end</code> parameters if they're not passed correctly
	 *
	 * @param i     the integer
	 * @param start range start (inclusive)
	 * @param end   range end (inclusive)
	 * @return whether the given integer is in range or not
	 */
	public static boolean inRange(
		int i,
		int start,
		int end
	) {
		if (start < end)
			return start <= i && i <= end;
		else /* end < start */
			return end <= i && i <= start;
	}
	
	/**
	 * returns the first element of the given list
	 *
	 * @return the first item in the list
	 * @throws IndexOutOfBoundsException if the list is empty
	 */
	public static <E> E first(@NotNull List<E> list) { return list.get(0); }
	
	/**
	 * returns the last element of the given list
	 *
	 * @return the last item in the list
	 * @throws IndexOutOfBoundsException if the list is empty
	 */
	public static <E> E last(@NotNull List<E> list) { return list.get(list.size() - 1); }
	
	/**
	 * safely returns {@link Character} from {@link CharSequence} or <code>null</code> if given index is out of bounds.
	 *
	 * @param charSequence the array to get from
	 * @param i            index
	 * @return {@link Character} or <code>null</code> if index is out of bounds
	 */
	public static @Nullable Character safeGet(CharSequence charSequence, int i) { return 0 <= i && i < charSequence.length() ? charSequence.charAt(i) : null; }
	
	/**
	 * copies the given string to system clipboard
	 *
	 * @param s the string to be copied
	 */
	public static void copyToClipboard(String s) { Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null); }
}
