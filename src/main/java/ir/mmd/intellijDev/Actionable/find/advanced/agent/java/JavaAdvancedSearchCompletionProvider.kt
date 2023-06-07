package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.openapi.project.Project
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchCompletionProvider
import ir.mmd.intellijDev.Actionable.util.ext.equals

object JavaAdvancedSearchCompletionProvider : AdvancedSearchCompletionProvider {
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
}
