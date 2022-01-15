package ir.mmd.intellijDev.Actionable.internal;

import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.util.NlsActions;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This class provides convenient way for bug report at Actionable's GitHub repo
 */
public class ErrorReporter extends ErrorReportSubmitter {
	@Override
	public @NlsActions.ActionText @NotNull String getReportActionText() { return "Create Issue on Github"; }
	
	@Override
	public boolean submit(
		IdeaLoggingEvent @NotNull [] events,
		@Nullable String additionalInfo,
		@NotNull Component parentComponent,
		@NotNull Consumer<? super SubmittedReportInfo> consumer
	) {
		try {
			final var title = URLEncoder.encode(
				"[BUG]: ",
				StandardCharsets.UTF_8
			);
			final var body = URLEncoder.encode(
				additionalInfo + "\n\n<details><pre>" + events[0].getThrowableText() + "</pre></details>",
				StandardCharsets.UTF_8
			);
			Desktop.getDesktop().browse(
				URI.create("https://github.com/MohammadMD1383/Actionable/issues/new?title=" + title + "&body=" + body)
			);
			
			consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE));
			return true;
		} catch (IOException e) {
			consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.FAILED));
			return false;
		}
	}
}
