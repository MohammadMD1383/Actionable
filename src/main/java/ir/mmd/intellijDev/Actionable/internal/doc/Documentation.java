package ir.mmd.intellijDev.Actionable.internal.doc;

import com.intellij.openapi.util.NlsContexts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Documentation {
	@NlsContexts.DialogTitle
	String title();
	
	@NlsContexts.DialogMessage
	String description();
	
	String example();
}
