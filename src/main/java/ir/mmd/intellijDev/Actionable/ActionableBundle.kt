package ir.mmd.intellijDev.Actionable

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "strings"

object ActionableBundle : DynamicBundle(BUNDLE) {
	fun string(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
		getMessage(key, *params)
	
	@Suppress("unused")
	fun stringPointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
		getLazyMessage(key, *params)
}
