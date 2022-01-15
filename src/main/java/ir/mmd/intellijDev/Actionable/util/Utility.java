package ir.mmd.intellijDev.Actionable.util;

public class Utility {
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
