package ir.mmd.intellijDev.Actionable.find.advanced.agent

/**
 * **Note:** This may be used even in *dumb mode*, when indexing is not finished yet.
 */
abstract class AdvancedSearchCompletionProvider {
	open fun getTopLevelProperties(): List<String> = emptyList()
	
	open fun getValuesForProperty(property: String): List<String> = emptyList()
	
	/**
	 * @param context see [AdvancedSearchContext]
	 *
	 * @return variables **without** `$`
	 */
	open fun getVariables(context: AdvancedSearchContext): List<String> = emptyList()
	
	/**
	 * @param context see [AdvancedSearchContext]
	 *
	 * @return a list containing the completion item and a boolean indicating that this
	 * completion item accepts parameters or not.
	 */
	open fun getIdentifiers(context: AdvancedSearchContext): List<Pair<String, Boolean>> = emptyList()
	
	/**
	 * @param context see [AdvancedSearchContext]
	 */
	open fun getParameters(context: AdvancedSearchContext): List<String> = emptyList()
}
