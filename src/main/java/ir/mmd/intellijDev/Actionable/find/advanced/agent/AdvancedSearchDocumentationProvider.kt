package ir.mmd.intellijDev.Actionable.find.advanced.agent

interface AdvancedSearchDocumentationProvider {
	/**
	 * the symbol:
	 *  * starts with `$` if it is a variable
	 *  * starts with `#` if it is a top-level property
	 *  * otherwise it is an identifier
	 *
	 *  @return documentation text that can be in html format or
	 *  null if there is no documentation available for the [symbol].
	 */
	fun getDocumentationFor(symbol: String): String?
}
