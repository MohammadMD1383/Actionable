package ir.mmd.intellijDev.Actionable.util

import com.intellij.util.application

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
 * executes [block] if `this` is `true`
 *
 * @return `this`
 */
inline infix fun Boolean.then(block: () -> Unit) = also { if (this) block() }

/**
 * Helper method to get application-wide service
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> service(clazz: Class<T>): T = application.getService(clazz)
