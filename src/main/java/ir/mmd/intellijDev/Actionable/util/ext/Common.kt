package ir.mmd.intellijDev.Actionable.util.ext

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Copies the string to clipboard
 */
fun String.copyToClipboard() = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(this), null)

/**
 * Checks if all characters in the string are distinct characters
 */
fun String.isAllDistinct() = toCharArray().distinct().size == length

/**
 * DSL for building strings using [StringBuilder]
 */
inline fun stringBuilder(block: StringBuilder.() -> Unit) = StringBuilder().apply(block).toString()

/**
 * `in` operator for checking nullable character existence in a string
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun String.contains(char: Char?) = char != null && indexOf(char) >= 0

/**
 * transforms first letter to upper-case
 */
inline val String.titleCase: String get() = replaceFirstChar { c -> c.uppercaseChar() }

/**
 * `in` operator for bound checking an [Int] in a nullable [IntRange]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange?.contains(i: Int) = this != null && contains(i)

/**
 * Checks if the number is **greater** than zero
 */
inline val Int.isPositive: Boolean get() = this > 0

/**
 * Same as kotlin `run` but doesn't return anything
 */
inline fun <T> T.runOnly(block: T.() -> Unit) = block()

/**
 * Same as kotlin `let` but doesn't return anything
 */
inline fun <T> T.letOnly(block: (T) -> Unit) = block(this)

/**
 * Sugar syntax to check if something is not null
 */
inline val <T> T.isNotNull: Boolean get() = this != null

/**
 * like `forEach` but brings the receiver to the scope
 */
inline fun <T> Iterable<T>.withEach(block: T.() -> Unit) = forEach { it.block() }

/**
 * like `forEach` but iterates over those items that meet the [condition]
 */
inline fun <T> Iterable<T>.forEachIf(condition: T.() -> Boolean, block: (T) -> Unit) = forEach { if (it.condition()) block(it) }

/**
 * like `forEach` but maps items like what `map` function does
 */
inline fun <T, R> Iterable<T>.forEachMapped(map: (T) -> R, block: (R) -> Unit) = forEach { block(map(it)) }

/**
 * like [forEachMapped] but brings the receiver to the scope
 */
inline fun <T, R> Iterable<T>.withEachMapped(map: (T) -> R, block: R.() -> Unit) = forEach { map(it).block() }

/**
 * Returns [IntRange.first]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange.component1() = first

/**
 * Returns [IntRange.last]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange.component2() = last
