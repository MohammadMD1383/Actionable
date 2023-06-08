package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.icons.AllIcons
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.patterns.*
import com.intellij.psi.*
import com.intellij.psi.impl.JavaPsiFacadeEx
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.util.ProcessingContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchAgent
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.util.wrap

class JavaAdvancedSearchAgent(project: Project, searchFile: AdvancedSearchFile) : AdvancedSearchAgent(project, searchFile) {
	override fun search(progress: ProgressIndicator, addResult: (SearchResult) -> Unit) {
		val scanSource = model.properties["scan-source"].toBoolean()
		
		if (model.statements.map { it.variable }.distinct().size != 1) {
			throw UnsupportedOperationException("all top level statements should have the same variable").wrap(true)
		}
		
		var criteria = buildCriteria(model.statements.first())
		progress.checkCanceled()
		
		if (model.statements.size > 1) {
			model.statements.subList(1, model.statements.size).forEach {
				progress.checkCanceled()
				criteria = criteria.and(buildCriteria(it))
			}
		}
		
		val total = PsiShortNamesCache.getInstance(project).allClassNames.size
		val searchScope = when (val scope = model.properties["scope"] ?: "all") {
			"project" -> GlobalSearchScope.projectScope(project)
			"all" -> GlobalSearchScope.allScope(project)
			else -> throw IllegalArgumentException("unknown scope: $scope").wrap(true)
		}
		
		progress.text = "Searching..."
		AllClassesSearch.search(searchScope, project).forEachIndexed { i, it ->
			progress.checkCanceled()
			progress.fraction = i.toDouble() / total
			
			when (criteria) {
				is PsiClassPattern -> {
					if (criteria.accepts(it)) {
						addResult(SearchResult(
							it.name!!,
							it.qualifiedName,
							it,
							getIconFor(it)
						))
					}
					
					if (scanSource) {
						it.navigationElement.acceptChildren(object : JavaRecursiveElementVisitor() {
							override fun visitClass(aClass: PsiClass) {
								if (aClass is PsiAnonymousClass) {
									super.visitClass(aClass)
								}
							}
							
							override fun visitAnonymousClass(aClass: PsiAnonymousClass) {
								if (criteria.accepts(aClass)) {
									addResult(SearchResult(
										"(anonymous)",
										"in ${it.qualifiedName}",
										aClass,
										getIconFor(aClass)
									))
								}
								
								super.visitAnonymousClass(aClass)
							}
						})
					}
				}
				
				is PsiMethodPattern -> {
					it.acceptChildren(object : JavaRecursiveElementVisitor() {
						override fun visitMethod(method: PsiMethod) {
							if (method.containingClass == it && criteria.accepts(method)) {
								addResult(SearchResult(
									method.name,
									"${it.qualifiedName}#${method.name}",
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
			null -> parent ?: throw IllegalArgumentException("both variable and parent cannot be null").wrap(true)
			"\$type" -> PsiJavaPatterns.psiClass()
			"\$class" -> PsiJavaPatterns.psiClass().nonInterface()
			"\$interface" -> PsiJavaPatterns.psiClass().isInterface().nonAnnotationType()
			"\$annotation" -> PsiJavaPatterns.psiClass().isAnnotationType()
			"\$method" -> PsiJavaPatterns.psiMethod()
			else -> throw IllegalArgumentException("unknown variable: ${statement.variable}").wrap(true)
		}
		
		when (statement.identifier) {
			null -> {}
			
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
			
			"super-of",
			"direct-super-of" -> {
				require(criteria is PsiClassPattern) { "only types can use ${statement.identifier}" }
				val direct = "direct" in statement.identifier
				statement.parameters.forEach {
					criteria = (criteria as PsiClassPattern).superOf(it, direct)
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
			
			"with-param" -> {
				require(criteria is PsiMethodPattern) { "only methods can use ${statement.identifier}" }
				criteria = (criteria as PsiMethodPattern).withParams(*statement.parameters.toTypedArray())
			}
			
			"has-param" -> {
				require(criteria is PsiMethodPattern) { "only methods can use ${statement.identifier}" }
				if (statement.parameters.isEmpty()) {
					criteria = (criteria as PsiMethodPattern).hasParam(null)
				} else {
					statement.parameters.forEach {
						criteria = (criteria as PsiMethodPattern).hasParam(it)
					}
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
			
			"not-anonymous" -> {
				require(criteria is PsiClassPattern) { "only types can use ${statement.identifier}" }
				criteria = (criteria as PsiClassPattern).nonAnonymous()
			}
			
			else -> throw IllegalArgumentException("unknown identifier: ${statement.identifier}").wrap(true)
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
			else -> throw UnsupportedOperationException("cannot mix `${child.javaClass.simpleName}` into ${parent.javaClass.simpleName}").wrap(true)
		}
		
		else -> throw UnsupportedOperationException("mixing is not supported for `${parent.javaClass.simpleName}` and `${child.javaClass.simpleName}`").wrap(true)
	}
}

private fun PsiClassPattern.inheritorOf(baseName: String, direct: Boolean) = with(object : PatternCondition<PsiClass>("inheritorOf") {
	override fun accepts(t: PsiClass, context: ProcessingContext): Boolean {
		val facade = JavaPsiFacadeEx.getInstanceEx(t.project)
		return t.isInheritor(facade.findClass(baseName), !direct)
	}
})

private fun PsiClassPattern.isAnonymous() = with(object : PatternCondition<PsiClass>("isAnonymous") {
	override fun accepts(t: PsiClass, context: ProcessingContext): Boolean {
		return t.name == null
	}
})

private fun PsiClassPattern.nonAnonymous() = with(object : PatternCondition<PsiClass?>("nonAnonymous") {
	override fun accepts(t: PsiClass, context: ProcessingContext?): Boolean {
		return t.name != null
	}
})

private fun PsiClassPattern.nonInterface() = with(object : PatternCondition<PsiClass>("nonInterface") {
	override fun accepts(t: PsiClass, context: ProcessingContext): Boolean {
		return !t.isInterface
	}
})

private fun PsiClassPattern.superOf(fqn: String, direct: Boolean) = with(object : PatternCondition<PsiClass?>("superOf") {
	override fun accepts(t: PsiClass, context: ProcessingContext?): Boolean {
		val facade = JavaPsiFacadeEx.getInstanceEx(t.project)
		return facade.findClass(fqn).isInheritor(t, !direct)
	}
})

private fun PsiMethodPattern.hasParam(param: String? = null) = with(object : PatternCondition<PsiMethod?>("withParam") {
	override fun accepts(t: PsiMethod, context: ProcessingContext?): Boolean {
		val parameterList = t.parameterList
		if (param == null) {
			return parameterList.parametersCount > 0
		}
		
		val split = param.split(" +".toRegex())
		return parameterList.parameters.any { p ->
			split.first() == p.type.canonicalText &&
				split.last().let { it == "_" || it == p.name }
		}
	}
})

private fun PsiMethodPattern.withParams(vararg param: String) = with(object : PatternCondition<PsiMethod?>("withParameters") {
	override fun accepts(t: PsiMethod, context: ProcessingContext?): Boolean {
		val parameterList = t.parameterList
		if (parameterList.parametersCount != param.size) {
			return false
		}
		
		val psiParameters = parameterList.parameters
		val regex = " +".toRegex()
		param.forEachIndexed { i, p ->
			val split = p.split(regex)
			if (psiParameters[i].type.canonicalText != split.first()) {
				return false
			}
			
			if (split.last().let { it != "_" && psiParameters[i].name != it }) {
				return false
			}
		}
		
		return true
	}
})

private val PsiClass.isExceptionClass: Boolean
	get() {
		val facade = JavaPsiFacadeEx.getInstanceEx(project)
		return isInheritor(facade.findClass("java.lang.Exception"), true)
	}
