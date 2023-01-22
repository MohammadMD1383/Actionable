package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.components.ServiceManager

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
 * if (...) return after {
 *   // do some stuff ...
 * }
 * ```
 */
inline fun after(block: () -> Unit) = block()

/**
 * example:
 * ```kotlin
 * if (...) return true after {
 *   // some cleanup ...
 * }
 * ```
 *
 * @see [after]
 */
inline infix fun Boolean.after(block: () -> Unit) = this.also { block() }

/**
 * Try and ignore any exceptions happens inside [block]
 */
inline fun tryCatching(block: () -> Unit) = try {
	block()
} catch (_: Exception) {
}

inline fun <reified T> service(): T = ServiceManager.getService(T::class.java)
inline fun <reified T, R> withService(block: T.() -> R) = ServiceManager.getService(T::class.java).block()
inline fun <T, R> withService(clazz: Class<T>, block: T.() -> R) = ServiceManager.getService(clazz).block()
