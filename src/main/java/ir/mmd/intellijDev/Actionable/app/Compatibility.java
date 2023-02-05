package ir.mmd.intellijDev.Actionable.app;

import com.intellij.openapi.application.ApplicationInfo;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to check compatibility of some actions with IDE versions if some actions have
 * some functionality that is dependent on the version of the IDE
 */
public final class Compatibility{
	private Compatibility() {
	}
	
	/**
	 * This class holds different versions of the IDE that are needed
	 */
	public static final class Version {
		/**
		 * Returns the current IDE version that plugin is running on
		 */
		public static final int[] currentVersion;
		
		static {
			final String[] strings = ApplicationInfo.getInstance().getBuild().asStringWithoutProductCodeAndSnapshot().split("\\.");
			currentVersion = new int[]{
				Integer.parseInt(strings[0]),
				Integer.parseInt(strings[1]),
				Integer.parseInt(strings[2])
			};
		}
	}
	
	/**
	 * This method will check if the current IDE version is higher or equal to the given {@code version}
	 */
	public static boolean isCompatibleWith(int @NotNull [] version) {
		int[] currentVersion = Version.currentVersion;
		
		if (currentVersion[0] < version[0])
			return false;
		
		if (currentVersion[0] > version[0])
			return true;
		
		if (currentVersion[1] < version[1])
			return false;
		
		if (currentVersion[1] > version[1])
			return true;
		
		return currentVersion[2] >= version[2];
	}
}
