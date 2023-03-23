package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.application.ApplicationManager

/**
 * You can use this to beatify the code like this:
 * ```kotlin
 * if (...) {
 *   // do some stuff ...
 *   return
 * }
 * ```
 * change that to:
 * ```kotlin
 * if (...) return afterDoing {
 *   // do some stuff ...
 * }
 * ```
 *
 * @see [Any.after]
 */
inline fun afterDoing(block: () -> Unit) = block()

/**
 * example:
 * ```kotlin
 * if (...) return true after {
 *   // some cleanup ...
 * }
 * ```
 *
 * @see [afterDoing]
 */
inline infix fun <T> T.after(block: () -> Unit) = also { block() }

/**
 * Try and ignore any exceptions happens inside [block]
 */
inline fun tryCatching(block: () -> Unit) = try {
	block()
} catch (_: Exception) {
}

/**
 * Helper method to get application-wide service
 */
inline fun <reified T> service(): T = ApplicationManager.getApplication().getService(T::class.java)

/**
 * Helper method to get application-wide service
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> service(clazz: Class<T>): T = ApplicationManager.getApplication().getService(clazz)
