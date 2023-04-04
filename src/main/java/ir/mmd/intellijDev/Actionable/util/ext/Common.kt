package ir.mmd.intellijDev.Actionable.util.ext

import ir.mmd.intellijDev.Actionable.util.StringCaseManipulator
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.nio.file.Path
import kotlin.io.path.Path

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
inline fun buildString(block: StringBuilder.() -> Unit) = StringBuilder().apply(block).toString()

/**
 * `in` operator for checking nullable character existence in a string
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun String.contains(char: Char?) = char != null && indexOf(char) >= 0

/**
 * Transforms the first letter to upper-case
 */
inline val String.titleCase: String get() = replaceFirstChar { c -> c.uppercaseChar() }

/**
 * Converts the case-style of the receiver to the case-style of the [other]
 *
 * @see ir.mmd.intellijDev.Actionable.util.StringCaseManipulator.CaseStyle
 * @see StringCaseManipulator
 */
@Suppress("NOTHING_TO_INLINE")
inline infix fun String.toCaseStyleOf(other: String): String {
	return StringCaseManipulator(this).toCaseStyleOf(other)
}

/**
 * Replaces all [ranges] in the string with the [replacement]
 *
 * **IMPORTANT:** The [ranges] must be sorted
 *
 * @return the new string and a new `ranges` that corresponds to replacement ranges in the resulting string.
 */
fun String.replaceRanges(ranges: List<IntRange>, replacement: String, preserveCase: Boolean = false): Pair<String, List<IntRange>> {
	val firstRange = ranges.firstOrNull() ?: return this to ranges
	val newRanges = ranges.toMutableList()
	val manipulator = StringCaseManipulator(replacement)
	val builder = StringBuilder()
	var rpl = if (preserveCase) manipulator.toCaseStyleOf(substring(firstRange)) else replacement
	
	builder.append(substring(0, firstRange.first))
	builder.append(rpl)
	newRanges[0] = firstRange.first..firstRange.first + rpl.length
	
	if (ranges.size == 1) {
		builder.append(substring(firstRange.last))
		return builder.toString() to newRanges
	}
	
	for (i in 1 until ranges.lastIndex) {
		val text = substring(ranges[i - 1].last, ranges[i].first)
		val newRangeStart = newRanges[i - 1].last + text.length
		rpl = if (preserveCase) manipulator.toCaseStyleOf(substring(ranges[i])) else replacement
		
		builder.append(text)
		builder.append(rpl)
		newRanges[i] = newRangeStart..newRangeStart + rpl.length
	}
	
	val lastRange = ranges.last()
	val text = substring(ranges[ranges.lastIndex - 1].last, lastRange.first)
	val newRangeStart = newRanges[ranges.lastIndex - 1].last + text.length
	rpl = if (preserveCase) manipulator.toCaseStyleOf(substring(lastRange)) else replacement
	
	builder.append(text)
	builder.append(rpl)
	builder.append(substring(lastRange.last))
	newRanges[ranges.lastIndex] = newRangeStart..newRangeStart + rpl.length
	
	return builder.toString() to newRanges
}

/**
 * `in` operator for bound checking an [Int] in a nullable [IntRange]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange?.contains(i: Int) = this != null && contains(i)

/**
 * Returns a new [IntRange] by `first + firstAddition .. last + lastAddition`
 */
@Suppress("NOTHING_TO_INLINE")
inline fun IntRange.copy(firstAddition: Int = 0, lastAddition: Int = 0) = first + firstAddition..last + lastAddition

/**
 * like `forEach` but brings the receiver to the scope
 */
inline fun <T> Iterable<T>.withEach(block: T.() -> Unit) = forEach { it.block() }

/**
 * like `forEachIndexed` but brings the receiver to the scope
 */
inline fun <T> Iterable<T>.withEachIndexed(block: T.(Int) -> Unit) = forEachIndexed { i, it -> it.block(i) }

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

/**
 * Checks whether an [IntRange] [other] is inside this [IntRange]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange.contains(other: IntRange) = first <= other.first && last >= other.last

/**
 * Joins two paths.
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun Path.plus(other: Path): Path = Path(this.toString(), other.toString())

/**
 * Joins a path and a string.
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun Path.plus(other: String): Path = Path(this.toString(), other)
