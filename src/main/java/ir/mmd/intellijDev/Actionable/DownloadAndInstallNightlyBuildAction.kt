package ir.mmd.intellijDev.Actionable

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAware
import com.intellij.util.io.HttpRequests
import com.intellij.util.io.ZipUtil
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.app.Actionable
import ir.mmd.intellijDev.Actionable.app.Actionable.AUTHOR
import ir.mmd.intellijDev.Actionable.app.Actionable.PLUGIN_NAME
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
				} catch (e: Exception) {
					Actionable.showNotification(project, "<b>Couldn't download nightly build</b><br><pre>${e.stackTraceToString()}</pre>")
				}
			}
		}
	)
	
	private fun getDownloadUrl(indicator: ProgressIndicator): String? {
		indicator.text = "Getting release info..."
		indicator.isIndeterminate = true
		
		val latestReleaseUrl = "https://api.github.com/repos/$AUTHOR/$PLUGIN_NAME/releases/latest"
		return HttpRequests.request(latestReleaseUrl)
			.throwStatusCodeException(true)
			.readString().let {
				"\"browser_download_url\":\"(.+?)\"".toRegex().find(it)?.groupValues?.get(1)
			}
	}
	
	private fun downloadPlugin(indicator: ProgressIndicator): Path {
		indicator.text = "Downloading..."
		
		val downloadUrl = getDownloadUrl(indicator) ?: throw IllegalStateException("Download url is null")
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
