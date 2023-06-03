package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.icons.AllIcons
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.patterns.PsiClassPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile

@Suppress("NonDefaultConstructor")
class JavaAdvancedSearchAgent(project: Project, searchFile: AdvancedSearchFile) : AdvancedSearchAgent(project, searchFile) {
	override fun search(progress: ProgressIndicator, addResult: (SearchResult) -> Unit) {
		var criteria = buildCriteria(model.statements.first())
		progress.checkCanceled()
		
		if (model.statements.size > 1) {
			model.statements.subList(1, model.statements.lastIndex).forEach {
				progress.checkCanceled()
				criteria = criteria.and(buildCriteria(it))
			}
		}
		
		progress.text = "Searching..."
		AllClassesSearch.search(GlobalSearchScope.allScope(project), project).forEach {
			progress.checkCanceled()
			if (criteria.accepts(it)) {
				addResult(SearchResult(
					it.name ?: "anonymous",
					it.qualifiedName,
					it,
					getIconFor(it)
				))
			}
		}
	}
	
	private fun getIconFor(element: PsiElement) = when (element) {
		is PsiClass -> AllIcons.Nodes.Class
		is PsiMethod -> AllIcons.Nodes.Method
		else -> null
	}
	
	private fun buildCriteria(statement: SearchStatement): PsiClassPattern {
		var criteria = when (statement.variable) {
			"\$class" -> PsiJavaPatterns.psiClass()
			"\$interface" -> PsiJavaPatterns.psiClass().isInterface
			else -> throw IllegalArgumentException("unknown variable: ${statement.variable}")
		}
		
		when (statement.identifier) {
			"extends",
			"implements" -> {
				statement.parameters.forEach {
					criteria = criteria.inheritorOf(true, it)
				}
			}
			
			else -> throw IllegalArgumentException("unknown identifier: ${statement.identifier}")
		}
		
		return criteria
	}
}
