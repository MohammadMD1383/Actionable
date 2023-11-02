package ir.mmd.intellijDev.Actionable.app

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

object Actionable {
	const val PLUGIN_ID: String = "ir.mmd.intellijDev.Actionable"
	const val PLUGIN_NAME: String = "Actionable"
	const val AUTHOR: String = "MohammadMD1383"
	
	const val NOTIFICATION_GROUP: String = "Actionable Notifications"
	const val DOCS_TOOLWINDOW_ID: String = "Actionable Docs"
	
	fun showNotification(project: Project, content: String, type: NotificationType) {
		Notifications.Bus.notify(
			Notification(NOTIFICATION_GROUP, content, type),
			project
		)
	}
	
	class UpToDateException : Exception()
}
