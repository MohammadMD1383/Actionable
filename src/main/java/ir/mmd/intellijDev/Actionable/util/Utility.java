package ir.mmd.intellijDev.Actionable.util;

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
}
