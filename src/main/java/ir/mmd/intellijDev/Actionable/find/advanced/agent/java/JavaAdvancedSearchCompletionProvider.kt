package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.openapi.project.Project
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchCompletionProvider

object JavaAdvancedSearchCompletionProvider: AdvancedSearchCompletionProvider {
	override fun getTopLevelProperties(project: Project): List<String> {
		return listOf("scope", "scan-source")
	}
}
