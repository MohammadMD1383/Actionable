package ir.mmd.intellijDev.Actionable.internal.proguard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({
	ElementType.TYPE,
	ElementType.FIELD,
	ElementType.METHOD
})
public @interface Keep {
}
