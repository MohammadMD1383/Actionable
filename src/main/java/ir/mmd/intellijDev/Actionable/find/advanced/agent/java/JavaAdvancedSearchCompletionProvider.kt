package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.openapi.project.Project
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchCompletionProvider
import ir.mmd.intellijDev.Actionable.util.ext.contains
import ir.mmd.intellijDev.Actionable.util.ext.equals

object JavaAdvancedSearchCompletionProvider : AdvancedSearchCompletionProvider() {
	override fun getTopLevelProperties(project: Project): List<String> {
		return listOf("scope", "scan-source")
	}
	
	override fun getValuesForProperty(project: Project, property: String): List<String> {
		return when (property) {
			"scope" -> listOf("project", "all")
			"scan-source" -> listOf("true", "false")
			else -> emptyList()
		}
	}
	
	override fun getVariables(project: Project, context: List<String>): List<String> {
		return when {
			context.isEmpty() -> listOf("type", "class", "interface", "annotation", "method")
			context[0].equals("\$type", "\$class", "\$interface", "\$annotation") -> listOf("method")
			else -> emptyList()
		}
	}
	
	override fun getIdentifiers(project: Project, variable: String?, context: List<String>): List<Pair<String, Boolean>> {
		return when {
			"\$method".equals(variable, context.getOrNull(0)) -> listOf(
				"has-param" to true,
				"with-param" to true,
				"name-matches" to true,
			)
			
			listOf("\$class", "\$interface", "\$type").contains(variable, context.getOrNull(0)) -> listOf(
				"super-of" to true,
				"extends" to true,
				"implements" to true,
				"direct-super-of" to true,
				"extends-directly" to true,
				"implements-directly" to true,
				"has-modifier" to true,
				"has-method" to true,
				"has-method-directly" to true,
				"name-matches" to true,
				"is-anonymous" to false,
				"not-anonymous" to false,
			)
			
			else -> emptyList()
		}
	}
	
	override fun getParameters(project: Project, variable: String?, identifier: String, context: List<String>): List<String> {
		return when {
			listOf("\$class", "\$type", "\$interface", "\$annotation").contains(variable ?: context.getOrNull(0)) &&
				identifier == "has-modifier" -> listOf("public", "protected", "private")
			
			else -> emptyList()
		}
	}
}
