package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.icons.AllIcons
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PsiClassPattern
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.JavaPsiFacadeEx
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import javax.swing.Icon

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
		
		val searchScope = when (val scope = model.properties["scope"] ?: "all") {
			"project" -> GlobalSearchScope.projectScope(project)
			"all" -> GlobalSearchScope.allScope(project)
			else -> throw IllegalArgumentException("unknown scope: $scope")
		}
		
		progress.text = "Searching..."
		AllClassesSearch.search(searchScope, project).forEach {
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
	
	private fun getIconFor(el: PsiClass): Icon? {
		return when {
			el.isExceptionClass -> if (el.hasModifierProperty("abstract")) AllIcons.Nodes.AbstractException else AllIcons.Nodes.ExceptionClass
			else -> el.getIcon(Iconable.ICON_FLAG_READ_STATUS)
		}
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
			
			"name-matches" -> {
				statement.parameters.forEach {
					criteria = criteria.withName(StandardPatterns.string().matches(it))
				}
			}
			
			"is-anonymous" -> {
				criteria = criteria.isAnonymous()
			}
			
			else -> throw IllegalArgumentException("unknown identifier: ${statement.identifier}")
		}
		
		return criteria
	}
}

fun PsiClassPattern.inheritorOf(baseName: String, direct: Boolean) = with(object : PatternCondition<PsiClass?>("inheritorOf") {
	override fun accepts(t: PsiClass, context: ProcessingContext?): Boolean {
		val facade = JavaPsiFacadeEx.getInstanceEx(t.project)
		return t.isInheritor(facade.findClass(baseName), !direct)
	}
})

fun PsiClassPattern.isAnonymous() = with(object : PatternCondition<PsiClass?>("isAnonymous") {
	override fun accepts(t: PsiClass, context: ProcessingContext?): Boolean {
		return t.name == null
	}
})

val PsiClass.isExceptionClass: Boolean
	get() {
		val facade = JavaPsiFacadeEx.getInstanceEx(project)
		return isInheritor(facade.findClass("java.lang.Exception"), true)
	}
