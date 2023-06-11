package ir.mmd.intellijDev.Actionable.find.advanced.agent.java

import com.intellij.lang.java.JavaLanguage
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchInjectionProvider
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchInjectionProvider.InjectionDescriptor
import ir.mmd.intellijDev.Actionable.find.advanced.agent.invoke

object JavaAdvancedSearchInjectionProvider : AdvancedSearchInjectionProvider {
	override fun getInjectionFor(context: AdvancedSearchContext): InjectionDescriptor? {
		return when {
			context[0..1] {
				variable equalTo "\$class" or "\$interface" or "\$type"
			} and context[0] {
				identifier equalTo "super-of" or "direct-super-of"
			} -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A<T extends ", "> { }"
			)
			
			context[0..1] {
				variable equalTo "\$method"
			} and context[0] {
				identifier equalTo "has-param" or "with-param"
			} -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A { void m(", ") { } }"
			)
			
			context[0..1] {
				variable equalTo "\$class"
			} and context[0] {
				identifier equalTo "extends" or "extends-directly"
			} -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A extends ", " { }"
			)
			
			context[0..1] {
				variable equalTo "\$class"
			} and context[0] {
				identifier equalTo "implements" or "implements-directly"
			} || context[0..1] {
				variable equalTo "\$interface"
			} and context[0] {
				identifier equalTo "extends" or "extends-directly"
			} -> InjectionDescriptor(
				JavaLanguage.INSTANCE,
				"class A implements ", " { }"
			)
			
			else -> null
		}
	}
}
