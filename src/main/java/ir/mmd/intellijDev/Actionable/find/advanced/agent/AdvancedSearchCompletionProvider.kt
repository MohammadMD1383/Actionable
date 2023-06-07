package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.openapi.project.Project

/**
 * **Note:** This may be used even in *dumb mode*, when indexing is not yet finished.
 */
interface AdvancedSearchCompletionProvider {
	fun getTopLevelProperties(project: Project): List<String>
	
	fun getValuesForProperty(project: Project, property: String): List<String>
	
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
	fun getVariables(project: Project, context: List<String>): List<String>
	
	/**
	 * @param variable the variable of current statement or null if not specified.
	 * @param context see [getVariables]
	 *
	 * @return a list containing the completion item and a boolean indicating that this
	 * completion item accepts parameters or not.
	 */
	fun getIdentifiers(project: Project, variable: String?, context: List<String>): List<Pair<String, Boolean>>
}
