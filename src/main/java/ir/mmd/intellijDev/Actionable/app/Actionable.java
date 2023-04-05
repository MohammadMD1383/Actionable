package ir.mmd.intellijDev.Actionable.app;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class Actionable {
	private Actionable() {
	}
	
	public static final String PLUGIN_ID = "ir.mmd.intellijDev.Actionable";
	public static final String PLUGIN_NAME = "Actionable";
	public static final String AUTHOR = "MohammadMD1383";
	
	public static final String NOTIFICATION_GROUP = "Actionable Notifications";
	public static final String DOCS_TOOLWINDOW_ID = "Actionable Docs";
	
	public static void showNotification(@NotNull Project project, String content, NotificationType type) {
		Notifications.Bus.notify(
			new Notification(NOTIFICATION_GROUP, content, type),
			project
		);
	}
	
	public static class UpToDateException extends Exception {
	}
}
