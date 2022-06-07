package ir.mmd.intellijDev.Actionable.internal

import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.util.Consumer
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import java.awt.Component
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.net.URLEncoder

@Keep
class ErrorReporter : ErrorReportSubmitter() {
	override fun getReportActionText() = "Create Issue on Github"
	
	override fun submit(
		events: Array<out IdeaLoggingEvent>,
		additionalInfo: String?,
		parentComponent: Component,
		consumer: Consumer<in SubmittedReportInfo>
	): Boolean = try {
		val title = URLEncoder.encode("[BUG]: ", "UTF-8")
		val body = URLEncoder.encode("$additionalInfo\n\n<details><pre>${events[0].throwableText}</pre></details>", "UTF-8")
		Desktop.getDesktop().browse(URI.create("https://github.com/MohammadMD1383/Actionable/issues/new?title=$title&body=$body"))
		
		consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE))
		true
	} catch (e: IOException) {
		consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.FAILED))
		false
	}
}
