package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.project.Project
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchInjectionProvider
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchInjectionProvider.InjectionDescriptor
import ir.mmd.intellijDev.Actionable.util.ext.equals

object JavaAdvancedSearchInjectionProvider : AdvancedSearchInjectionProvider {
	override fun getInjectionFor(project: Project, variable: String?, identifier: String, context: List<String>): InjectionDescriptor? {
		return when {
			(variable ?: context.getOrNull(0)).equals("\$class", "\$interface", "\$type") &&
				identifier.equals("super-of", "direct-super-of") -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A<T extends ", "> { }"
			)
			
			(variable ?: context.getOrNull(0)) == "\$method" &&
				identifier.equals("has-param", "with-param") -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A { void m(", ") { } }"
			)
			
			(variable ?: context.getOrNull(0)) == "\$class" &&
				identifier.equals("extends", "extends-directly") -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A extends ", " { }"
			)
			
			((variable ?: context.getOrNull(0)) == "\$class" && identifier.equals("implements", "implements-directly")) ||
				((variable ?: context.getOrNull(0)) == "\$interface" && identifier.equals("extends", "extends-directly")) -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A implements ", " { }"
			)
			
			else -> null
		}
	}
}
