package ir.mmd.intellijDev.Actionable.util.ext

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

fun String.copyToClipboard() = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(this), null)
fun String.isAllDistinct() = toCharArray().distinct().size == length

inline fun stringBuilder(block: StringBuilder.() -> Unit) = StringBuilder().apply(block).toString()
inline operator fun String.contains(char: Char?) = char != null && indexOf(char) >= 0
inline val String.titleCase: String get() = replaceFirstChar { c -> c.uppercaseChar() }
inline operator fun IntRange?.contains(i: Int) = this != null && contains(i)

inline val Int.isPositive: Boolean get() = this > 0
inline operator fun Int.plus(block: Int.() -> Int) = plus(block())

inline fun <T> T.runOnly(block: T.() -> Unit) = block()
inline fun <T> T.letOnly(block: (T) -> Unit) = block(this)

inline val <T> T.isNotNull: Boolean get() = this != null

inline fun <T, O1, O2> T.applyWith(o1: O1, o2: O2, block: T.(O1, O2) -> Unit) = apply { block(o1, o2) }

inline fun <T, R> Iterable<T>.forEachWith(receiver: R, block: R.(T) -> Unit) = forEach { receiver.block(it) }
inline fun <T> Iterable<T>.withEach(block: T.() -> Unit) = forEach { it.block() }
inline fun <T> Iterable<T>.forEachIf(condition: T.() -> Boolean, block: (T) -> Unit) = forEach { if (it.condition()) block(it) }

inline operator fun IntRange.component1() = first
inline operator fun IntRange.component2() = last

inline fun doIf(condition: () -> Boolean, block: () -> Unit) {
	if (condition()) block()
}
