package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.icons.AllIcons
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.patterns.*
import com.intellij.psi.*
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
	
	private fun buildCriteria(statement: SearchStatement, parent: PsiElementPattern<out PsiElement, *>? = null): PsiElementPattern<out PsiElement, *> {
		var criteria: PsiElementPattern<out PsiElement, *> = when (statement.variable) {
			"\$type" -> PsiJavaPatterns.psiClass()
			"\$class" -> PsiJavaPatterns.psiClass().nonInterface()
			"\$interface" -> PsiJavaPatterns.psiClass().isInterface().nonAnnotationType()
			"\$annotation" -> PsiJavaPatterns.psiClass().isAnnotationType()
			"\$method" -> PsiJavaPatterns.psiMethod()
			null -> parent ?: throw IllegalArgumentException("both variable and parent cannot be null")
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
				require(criteria is PsiMemberPattern) { "only classes and interfaces and methods can use ${statement.identifier}" }
				criteria = (criteria as PsiMemberPattern<out PsiMember, *>).withModifiers(*statement.parameters.toTypedArray())
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
		
		statement.innerStatements?.forEach {
			criteria = if (it.variable != null) {
				mixCriteria(criteria, buildCriteria(it))
			} else {
				criteria.and(buildCriteria(it, criteria))
			}
		}
		
		return criteria
	}
	
	private fun mixCriteria(parent: PsiElementPattern<out PsiElement, *>, child: PsiElementPattern<out PsiElement, *>) = when (parent) {
		is PsiClassPattern -> when (child) {
			is PsiMethodPattern -> parent.withMethod(true, child)
			else -> throw UnsupportedOperationException("cannot mix `${child.javaClass.simpleName}` into ${parent.javaClass.simpleName}")
		}
		
		else -> throw UnsupportedOperationException("mixing is not supported for `${parent.javaClass.simpleName}` and `${child.javaClass.simpleName}`")
	}
}

fun PsiClassPattern.inheritorOf(baseName: String, direct: Boolean) = with(object : PatternCondition<PsiClass>("inheritorOf") {
	override fun accepts(t: PsiClass, context: ProcessingContext): Boolean {
		val facade = JavaPsiFacadeEx.getInstanceEx(t.project)
		return t.isInheritor(facade.findClass(baseName), !direct)
	}
})

fun PsiClassPattern.isAnonymous() = with(object : PatternCondition<PsiClass>("isAnonymous") {
	override fun accepts(t: PsiClass, context: ProcessingContext): Boolean {
		return t.name == null
	}
})

fun PsiClassPattern.nonInterface() = with(object : PatternCondition<PsiClass>("nonInterface") {
	override fun accepts(t: PsiClass, context: ProcessingContext): Boolean {
		return !t.isInterface
	}
})

val PsiClass.isExceptionClass: Boolean
	get() {
		val facade = JavaPsiFacadeEx.getInstanceEx(project)
		return isInheritor(facade.findClass("java.lang.Exception"), true)
	}
