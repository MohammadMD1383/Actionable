package ir.mmd.intellijDev.Actionable

import com.intellij.ide.plugins.PluginManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAware
import com.intellij.util.io.HttpRequests
import com.intellij.util.io.ZipUtil
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.app.Actionable.*
import ir.mmd.intellijDev.Actionable.util.ext.plus
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteRecursively

class DownloadAndInstallNightlyBuildAction : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() = ProgressManager.getInstance().run(
		object : Task.Backgroundable(project, "Downloading nightly release...") {
			override fun run(indicator: ProgressIndicator) {
				try {
					installPlugin(indicator)
					showNotification(project, "<b>Actionable installed successfully!</b><br>You need to restart the IDE before using the new version", NotificationType.INFORMATION)
				} catch (e: UpToDateException) {
					showNotification(project, "<b>You have already installed the latest version!</b>", NotificationType.INFORMATION)
				} catch (e: Exception) {
					showNotification(project, "<b>Couldn't download nightly build</b><br><pre>${e.stackTraceToString()}</pre>", NotificationType.ERROR)
				}
			}
		}
	)
	
	private fun getDownloadUrl(indicator: ProgressIndicator): String {
		indicator.text = "Getting release info..."
		indicator.isIndeterminate = true
		
		val latestReleaseUrl = "https://api.github.com/repos/$AUTHOR/$PLUGIN_NAME/releases/latest"
		val json = HttpRequests.request(latestReleaseUrl)
			.throwStatusCodeException(true)
			.readString()
		
		val tag = "\"tag_name\":\"(.+?)\"".toRegex().find(json)?.groupValues?.get(1)
			?: throw Exception("Couldn't find tag_name from response")
		val version = PluginManager.getPlugins().find { it.pluginId.idString == PLUGIN_ID }!!.version
		if (version == tag) {
			throw UpToDateException()
		}
		
		return "\"browser_download_url\":\"(.+?)\"".toRegex().find(json)?.groupValues?.get(1)
			?: throw Exception("Couldn't find download_url from response")
	}
	
	private fun downloadPlugin(indicator: ProgressIndicator): Path {
		val downloadUrl = getDownloadUrl(indicator)
		
		indicator.text = "Downloading..."
		
		val tmpFile = createTempFile()
		
		HttpRequests.request(downloadUrl)
			.throwStatusCodeException(true)
			.saveToFile(tmpFile, indicator)
		
		return tmpFile
	}
	
	@OptIn(ExperimentalPathApi::class)
	private fun installPlugin(indicator: ProgressIndicator) {
		val tmpPluginPath = downloadPlugin(indicator)
		
		indicator.text = "Installing..."
		indicator.isIndeterminate = true
		
		val pluginsPath = PathManager.getPluginsDir()
		(pluginsPath + PLUGIN_NAME).deleteRecursively()
		ZipUtil.extract(tmpPluginPath, pluginsPath, null)
	}
}
