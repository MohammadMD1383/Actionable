package ir.mmd.intellijDev.Actionable

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefBrowser
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.app.Actionable
import ir.mmd.intellijDev.Actionable.util.ext.plus
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefRequestHandlerAdapter
import org.cef.network.CefRequest
import java.awt.Desktop
import java.net.URI
import com.intellij.openapi.wm.ToolWindowFactory as JBToolWindowFactory

class OpenOfflineHelpAction : ActionBase() {
	context (LazyEventContext)
	override fun performAction() {
		ToolWindowManager
			.getInstance(project)
			.getToolWindow(Actionable.DOCS_TOOLWINDOW_ID)
			?.show()
	}
	
	class ToolWindowFactory : JBToolWindowFactory, DumbAware {
		override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
			val contentFactory = ContentFactory.getInstance()
			val docsRoot = (PathManager.getPluginsDir() + Actionable.PLUGIN_NAME + "docs").toAbsolutePath()
			val webView = JBCefBrowser((docsRoot + "index.html").toUri().toString())
			val toolWindowContent = contentFactory.createContent(webView.component, "Actionable Offline Docs", true)
			
			toolWindow.contentManager.addContent(toolWindowContent)
			webView.jbCefClient.addRequestHandler(
				object : CefRequestHandlerAdapter() {
					override fun onBeforeBrowse(browser: CefBrowser, frame: CefFrame, request: CefRequest, user_gesture: Boolean, is_redirect: Boolean): Boolean {
						if (!request.url.startsWith(docsRoot.toUri().toString())) {
							Desktop.getDesktop().browse(URI.create(request.url))
							return true
						}
						
						return false
					}
				}, webView.cefBrowser
			)
		}
	}
}
