package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.components.ServiceManager

inline fun <T> by(receiver: T, block: (T) -> Unit) = block(receiver)
inline fun <T, R> returnBy(obj: T, block: (T) -> R) = block(obj)
inline fun <T1, T2, R> returnBy(obj1: T1, obj2: T2, block: (T1, T2) -> R) = block(obj1, obj2)

inline fun <T, O, R> with(receiver: T, obj: O, block: T.(O) -> R) = receiver.block(obj)

inline fun after(block: () -> Unit) = block()
inline fun trueAfter(block: () -> Unit): Boolean {
	block()
	return true
}

inline fun <T> nonnull(receiver: T?, block: (T) -> Unit) {
	if (receiver != null) block(receiver)
}

inline fun tryCatching(block: () -> Unit) = try {
	block()
} catch (_: Throwable) {
}

inline fun <reified T> service(): T = ServiceManager.getService(T::class.java)
inline fun <reified T, R> withService(block: T.() -> R) = ServiceManager.getService(T::class.java).block()
