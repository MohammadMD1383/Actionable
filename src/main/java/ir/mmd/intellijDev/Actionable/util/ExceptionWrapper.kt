package ir.mmd.intellijDev.Actionable.util

class ExceptionWrapper(
	val original: Exception,
	val shouldBeCaught: Boolean
) : Exception() {
	fun throwIfShouldNotBeCaught() {
		if (!shouldBeCaught) {
			throw original
		}
	}
}

fun Exception.wrap(shouldBeCaught: Boolean) = ExceptionWrapper(this, shouldBeCaught)
