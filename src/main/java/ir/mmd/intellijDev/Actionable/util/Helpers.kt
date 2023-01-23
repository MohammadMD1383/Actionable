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
 * brings an application-wide service to scope of [block]
 *
 * @see [service]
 * @see [withService]
 */
inline fun <reified T, R> withService(block: T.() -> R) = ApplicationManager.getApplication().getService(T::class.java).block()

/**
 * @see [withService]
 */
inline fun <T, R> withService(clazz: Class<T>, block: T.() -> R) = ApplicationManager.getApplication().getService(clazz).block()
