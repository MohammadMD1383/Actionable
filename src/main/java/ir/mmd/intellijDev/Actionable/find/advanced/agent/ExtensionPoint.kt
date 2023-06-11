package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.util.xmlb.Converter
import com.intellij.util.xmlb.annotations.Attribute

val AdvancedSearchExtensionPoint = ExtensionPointName.create<AdvancedSearchProviderFactoryBean>("ir.mmd.intellijDev.Actionable.advancedSearch.providerFactory")

class AdvancedSearchProviderFactoryBean {
	/**
	 * **Required**
	 *
	 * This is the `id` of the language that you want to provide
	 * the advanced search functionality for.
	 */
	@Attribute("language")
	lateinit var language: String
	
	/**
	 * **Required**
	 *
	 * FQN of an implementation of [AdvancedSearchExtensionFactory].
	 */
	@Attribute("factoryClass", converter = InstanceConverter::class)
	lateinit var factoryClass: AdvancedSearchExtensionFactory
	
	/* ---------------------------------------------------------------------------------------------------- */
	
	private class InstanceConverter : Converter<Any>() {
		override fun toString(value: Any) = null
		override fun fromString(value: String): Any {
			return Class.forName(value).getConstructor().newInstance()
		}
	}
}
