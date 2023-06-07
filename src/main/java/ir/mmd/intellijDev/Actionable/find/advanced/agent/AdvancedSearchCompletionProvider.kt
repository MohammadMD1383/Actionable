package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.openapi.project.Project

/**
 * **Note:** This may be used even in *dumb mode*, when indexing is not yet finished.
 */
abstract class AdvancedSearchCompletionProvider {
	open fun getTopLevelProperties(project: Project): List<String> = emptyList()
	
	open fun getValuesForProperty(project: Project, property: String): List<String> = emptyList()
	
	/**
	 * @param context will contain parent `$variable`s or `identifier`s if `$variable` is
	 * not present.
	 *
	 * for instance, having:
	 * ```AdvancedSearch
	 * $file {
	 *   has-class 'SomeClass' {
	 *     $method name-matches '<regexp>' {
	 *       $<completion-place>
	 *     }
	 *   }
	 * }
	 * ```
	 * will provide a context containing: `['$method', 'has-class', '$file']`
	 *
	 * **Notice:** [context] may contain empty strings, but that shouldn't happen!
	 *
	 * @return variables **without** `$`
	 */
	open fun getVariables(project: Project, context: List<String>): List<String> = emptyList()
	
	/**
	 * @param variable the variable of current statement or null if not specified.
	 * @param context see [getVariables]
	 *
	 * @return a list containing the completion item and a boolean indicating that this
	 * completion item accepts parameters or not.
	 */
	open fun getIdentifiers(project: Project, variable: String?, context: List<String>): List<Pair<String, Boolean>> = emptyList()
	
	/**
	 * @param variable see [getIdentifiers]
	 * @param identifier identifier of the current statement
	 * @param context see [getVariables]
	 */
	open fun getParameters(project: Project, variable: String?, identifier: String, context: List<String>): List<String> = emptyList()
}
