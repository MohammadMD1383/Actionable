package ir.mmd.intellijDev.Actionable.util

/**
 * use [wrap] for convenience to wrap an exception if coding in kotlin.
 *
 * a wrapped exception that [shouldBeCaught], means that this exception is handled
 * and an error message with the details of this exception is shown to the user.
 *
 * any fatal error that should really terminate the process must set the [shouldBeCaught]
 * to `false`.
 */
class WrappedException(
	val original: Exception,
	val shouldBeCaught: Boolean
) : Exception() {
	/**
	 * throws the [original] exception if [shouldBeCaught] is `false`
	 */
	@Throws(Exception::class)
	fun throwIfShouldNotBeCaught() {
		if (!shouldBeCaught) {
			throw original
		}
	}
}

fun Exception.wrap(shouldBeCaught: Boolean = true) = WrappedException(this, shouldBeCaught)
