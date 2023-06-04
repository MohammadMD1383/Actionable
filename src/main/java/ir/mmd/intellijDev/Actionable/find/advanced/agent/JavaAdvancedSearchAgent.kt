package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.icons.AllIcons
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.patterns.*
import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import com.intellij.psi.impl.JavaPsiFacadeEx
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile

@Suppress("NonDefaultConstructor")
class JavaAdvancedSearchAgent(project: Project, searchFile: AdvancedSearchFile) : AdvancedSearchAgent(project, searchFile) {
	override fun search(progress: ProgressIndicator, addResult: (SearchResult) -> Unit) {
		if (model.statements.map { it.variable }.distinct().size != 1) {
			throw IllegalArgumentException("all top level statements should have the same variable")
		}
		
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
			
			when (criteria) {
				is PsiClassPattern -> if (criteria.accepts(it)) {
					addResult(SearchResult(
						it.name ?: "anonymous",
						it.qualifiedName,
						it,
						getIconFor(it)
					))
				}
				
				is PsiMethodPattern -> {
					it.acceptChildren(object : JavaRecursiveElementVisitor() {
						override fun visitMethod(method: PsiMethod) {
							if (criteria.accepts(method)) {
								addResult(SearchResult(
									method.name,
									"${method.containingClass!!.qualifiedName}#${method.name}",
									method,
									getIconFor(method)
								))
							}
						}
					})
				}
			}
		}
	}
	
	private fun getIconFor(el: PsiMember) = when {
		el is PsiClass && el.isExceptionClass -> {
			if (el.hasModifierProperty("abstract")) {
				AllIcons.Nodes.AbstractException
			} else {
				AllIcons.Nodes.ExceptionClass
			}
		}
		
		else -> el.getIcon(Iconable.ICON_FLAG_READ_STATUS)
	}
	
	private fun buildCriteria(statement: SearchStatement): PsiMemberPattern<*, *> {
		var criteria: PsiMemberPattern<*, *> = when (statement.variable) {
			"\$class" -> PsiJavaPatterns.psiClass()
			"\$interface" -> PsiJavaPatterns.psiClass().isInterface
			"\$method" -> PsiJavaPatterns.psiMethod()
			else -> throw IllegalArgumentException("unknown variable: ${statement.variable}")
		}
		
		when (statement.identifier) {
			"extends",
			"implements",
			"extends-directly",
			"implements-directly" -> {
				require(criteria is PsiClassPattern) { "only classes and interfaces can use ${statement.identifier}" }
				val direct = "directly" in statement.identifier
				statement.parameters.forEach {
					criteria = (criteria as PsiClassPattern).inheritorOf(it, direct)
				}
			}
			
			"has-method",
			"has-method-directly" -> {
				require(criteria is PsiClassPattern) { "only classes and interfaces can use ${statement.identifier}" }
				val checkDeep = "directly" !in statement.identifier
				statement.parameters.map {
					PsiJavaPatterns.psiMethod().withName(it)
				}.forEach {
					criteria = (criteria as PsiClassPattern).withMethod(checkDeep, it)
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
				require(criteria is PsiClassPattern) { "only classes can use ${statement.identifier}" }
				criteria = (criteria as PsiClassPattern).isAnonymous()
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
