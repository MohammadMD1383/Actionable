package ir.mmd.intellijDev.Actionable.find.advanced.agent

abstract class AdvancedSearchDocumentationProvider {
	/**
	 *  @return documentation text that can be in html format or
	 *  null if there is no documentation available for the [property].
	 */
	open fun getPropertyDocumentation(property: String): String? = null
	
	/**
	 * @param property indicates that this [value] is specified for which property in the code
	 *
	 *  @return documentation text that can be in html format or
	 *  null if there is no documentation available for the [value].
	 */
	open fun getPropertyValueDocumentation(property: String, value: String): String? = null
	
	/**
	 * @param context see [AdvancedSearchContext]
	 *
	 *  @return documentation text that can be in html format or
	 *  null if there is no documentation available for the [variable].
	 */
	open fun getVariableDocumentation(context: AdvancedSearchContext, variable: String): String? = null
	
	/**
	 * @param context see [AdvancedSearchContext]
	 *
	 *  @return documentation text that can be in html format or
	 *  null if there is no documentation available for the [identifier].
	 */
	open fun getIdentifierDocumentation(context: AdvancedSearchContext, identifier: String): String? = null
	
	/**
	 * @param context see [AdvancedSearchContext]
	 *
	 *  @return documentation text that can be in html format or
	 *  null if there is no documentation available for the [parameter].
	 */
	open fun getParameterDocumentation(context: AdvancedSearchContext, parameter: String): String? = null
}
