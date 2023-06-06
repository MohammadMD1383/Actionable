package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.openapi.project.Project

/**
 * **Note:** This may be used even in *dumb mode*, when indexing is not yet finished.
 */
interface AdvancedSearchCompletionProvider {
	fun getTopLevelProperties(project: Project): List<String>
}
