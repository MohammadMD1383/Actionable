package ir.mmd.intellijDev.Actionable.util.ext

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

fun String.copyToClipboard() = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(this), null)
fun String.isAllDistinct() = toCharArray().distinct().size == length

inline operator fun String.contains(char: Char?): Boolean = char != null && indexOf(char) >= 0
inline operator fun IntRange?.contains(i: Int): Boolean = this != null && contains(i)

inline val Int.isPositive: Boolean get() = this > 0
inline operator fun Int.plus(block: Int.() -> Int) = plus(block())

inline fun <T> T.runOnly(block: T.() -> Unit) = block()

inline fun <T, O1, O2> T.applyWith(o1: O1, o2: O2, block: T.(O1, O2) -> Unit) = apply { block(o1, o2) }

inline fun <T> Iterable<T>.withEach(block: T.() -> Unit) = forEach { it.block() }
inline fun <T> Iterable<T>.forEachIf(condition: T.() -> Boolean, block: (T) -> Unit) = forEach { if (it.condition()) block(it) }

inline fun doIf(condition: () -> Boolean, block: () -> Unit) {
	if (condition()) block()
}
