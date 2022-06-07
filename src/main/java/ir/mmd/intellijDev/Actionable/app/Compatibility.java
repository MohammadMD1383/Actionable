package ir.mmd.intellijDev.Actionable.app;

import com.intellij.openapi.application.ApplicationInfo;
import org.jetbrains.annotations.NotNull;

public final class Compatibility {
	public static final class Version {
		public static final int[] currentVersion;
		public static final int[] _193_5233_102_ = new int[]{193, 5233, 102};
		
		static {
			final String[] strings = ApplicationInfo.getInstance().getBuild().asStringWithoutProductCodeAndSnapshot().split("\\.");
			currentVersion = new int[]{
				Integer.parseInt(strings[0]),
				Integer.parseInt(strings[1]),
				Integer.parseInt(strings[2])
			};
		}
	}
	
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
