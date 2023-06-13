package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchDocumentationProvider
import ir.mmd.intellijDev.Actionable.find.advanced.agent.invoke

object JavaAdvancedSearchDocumentationProvider : AdvancedSearchDocumentationProvider() {
	override fun getPropertyDocumentation(property: String) = when (property) {
		"scope" -> """
				denotes the search scope:<br>
				<ul>
					<li>project: only files within your project. doesn't include any libraries</li>
					<li>all: everywhere</li>
				</ul>
			""".trimIndent()
		
		"scan-source" -> """
				whether or not to search in method bodies, etc.<br>
				if this is set to false, the search is performed using stubs, and not full sources.<br>
				you need to set this to true when e.g. searching for anonymous classes.
			""".trimIndent()
		
		else -> null
	}
	
	override fun getPropertyValueDocumentation(property: String, value: String) = when (property) {
		"scope" -> when (value) {
			"all" -> "searches everywhere"
			"project" -> "searches only in project files"
			else -> null
		}
		
		"scan-source" -> when (value) {
			"true" -> "search in source code"
			"false" -> "don't search in source code, just stubs"
			else -> null
		}
		
		else -> null
	}
	
	override fun getVariableDocumentation(context: AdvancedSearchContext, variable: String) = when (variable) {
		"\$type" -> "searches for any type: classes, interfaces, ..."
		"\$class" -> "searches for classes"
		"\$interface" -> "searches for interfaces. <b>not including annotations</b>"
		"\$annotation" -> "searches for annotation types. <b>the declarations, not usages</b>"
		"\$method" -> "searches for methods"
		else -> null
	}
	
	override fun getIdentifierDocumentation(context: AdvancedSearchContext, identifier: String) = when (identifier) {
		"has-param" -> """
				checks that this method has exactly all the parameters specified.<br>
				example: <code>java.lang.Runnable r</code><br>
				this check is performed against both type and the name,
				you can	use '_' to discard the check against the name.
			""".trimMargin()
		
		"with-param" -> "like <code>has-param</code>, but checks that at least these parameters are present"
		"name-matches" -> "checks that the name of the entry matches this regexp"
		"super-of" -> "checks that this type is the super type of all types specified in parameters"
		"extends" -> "checks that this type extends all the types specified in parameters"
		"implements" -> "checks that this type implements all the interfaces specified in parameters"
		"direct-super-of" -> "check that this type is directly the super type of all types specified in parameters"
		"extends-directly" -> "check that this type directly extends all the types specified in parameters"
		"implements-directly" -> "check that this type directly implements all the interfaces specified in parameters"
		"has-modifier" -> "checks that this entry has at least all the modifiers specified in parameters"
		"has-method" -> "checks that this type has methods with names specified in parameters"
		"has-method-directly" -> "checks that this type has methods that are defined in the type itself, not just inherited"
		"is-anonymous" -> "checks that this type is an anonymous class"
		"not-anonymous" -> "checks that this type is not an anonymous class"
		else -> null
	}
	
	override fun getParameterDocumentation(context: AdvancedSearchContext, parameter: String): String? {
		return when {
			context[0..1] {
				variable equalTo "\$class" or "\$type" or "\$interface" or "\$annotation"
			} and context[0] {
				identifier equalTo "has-modifier"
			} -> when (parameter) {
				"public" -> "java <code>public</code> access modifier"
				"private" -> "java <code>private</code> access modifier"
				"protected" -> "java <code>protected</code> access modifier"
				else -> null
			}
			
			else -> null
		}
	}
}
