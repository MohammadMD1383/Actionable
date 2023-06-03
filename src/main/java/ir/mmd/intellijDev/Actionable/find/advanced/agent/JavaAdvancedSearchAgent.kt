package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.icons.AllIcons
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PsiClassPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.impl.JavaPsiFacadeEx
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile

@Suppress("NonDefaultConstructor")
class JavaAdvancedSearchAgent(project: Project, searchFile: AdvancedSearchFile) : AdvancedSearchAgent(project, searchFile) {
	override fun search(progress: ProgressIndicator, addResult: (SearchResult) -> Unit) {
		var criteria = buildCriteria(model.statements.first())
		progress.checkCanceled()
		
		if (model.statements.size > 1) {
			model.statements.subList(1, model.statements.size).forEach {
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
			"implements",
			"extends-directly",
			"implements-directly" -> {
				val direct = "directly" in statement.identifier
				statement.parameters.forEach {
					criteria = criteria.inheritorOf(it, direct)
				}
			}
			
			"has-method",
			"has-method-directly" -> {
				val checkDeep = "directly" !in statement.identifier
				statement.parameters.map {
					PsiJavaPatterns.psiMethod().withName(it)
				}.forEach {
					criteria = criteria.withMethod(checkDeep, it)
				}
			}
			
			"has-modifier" -> {
				criteria = criteria.withModifiers(*statement.parameters.toTypedArray())
			}
			
			else -> throw IllegalArgumentException("unknown identifier: ${statement.identifier}")
		}
		
		return criteria
	}
}

fun PsiClassPattern.inheritorOf(baseName: String, direct: Boolean) = with(object : PatternCondition<PsiClass?>("directInheritorOf") {
	override fun accepts(t: PsiClass, context: ProcessingContext?): Boolean {
		val facade = JavaPsiFacadeEx.getInstanceEx(t.project)
		return t.isInheritor(facade.findClass(baseName), !direct)
	}
})
