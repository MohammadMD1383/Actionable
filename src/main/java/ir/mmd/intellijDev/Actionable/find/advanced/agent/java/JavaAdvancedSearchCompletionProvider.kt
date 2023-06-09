package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.openapi.project.Project
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchCompletionProvider
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.invoke

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
	
	override fun getVariables(project: Project, context: AdvancedSearchContext): List<String> {
		return when {
			context.atTopLevel -> listOf("type", "class", "interface", "annotation", "method")
			context[1] { variable equalTo "\$type" or "\$class" or "\$interface" or "\$annotation" } -> listOf("method")
			else -> emptyList()
		}
	}
	
	override fun getIdentifiers(project: Project, context: AdvancedSearchContext): List<Pair<String, Boolean>> {
		return when {
			context[0..1] { variable equalTo "\$method" } -> listOf(
				"has-param" to true,
				"with-param" to true,
				"name-matches" to true,
			)
			
			context[0..1] { variable equalTo "\$class" or "\$interface" or "\$type" } -> listOf(
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
	
	override fun getParameters(project: Project, context: AdvancedSearchContext): List<String> {
		return when {
			context[0..1] {
				variable equalTo "\$class" or "\$type" or "\$interface" or "\$annotation"
			} and context[0] {
				identifier equalTo "has-modifier"
			} -> listOf("public", "protected", "private")
			
			else -> emptyList()
		}
	}
}
