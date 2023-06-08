package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.util.xmlb.Converter
import com.intellij.util.xmlb.annotations.Attribute
import ir.mmd.intellijDev.Actionable.find.advanced.agent.java.JavaAdvancedSearchInjectionProvider

val AdvancedSearchExtensionPoint = ExtensionPointName.create<AdvancedSearchProviderBean>("ir.mmd.intellijDev.Actionable.advancedSearch.provider")

class AdvancedSearchProviderBean {
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
	 * This is the FQN of your class that implements [AdvancedSearchAgent].
	 */
	@Attribute("agentClass", converter = ConstructorConverter::class)
	lateinit var agentClass: Constructor<AdvancedSearchAgent>
	
	/**
	 * **Optional**
	 *
	 * You may provide a class **instance** implementing [AdvancedSearchCompletionProvider]
	 * to provide completions for the advanced search file when language property is set to [language].
	 */
	@Attribute("completionProviderInstance", converter = InstanceConverter::class)
	var completionProviderInstance: AdvancedSearchCompletionProvider? = null
	
	/**
	 * **Optional**
	 *
	 * You may provide an instance of annotator to be used when the advanced search file's
	 * language property is set to the [language].
	 */
	@Attribute("annotatorInstance", converter = InstanceConverter::class)
	var annotatorInstance: Annotator? = null
	
	/**
	 * **Optional**
	 *
	 * You may provide an instance of [AdvancedSearchDocumentationProvider] in order to
	 * render documentation for top-level properties, variables and identifiers when the
	 * file's language property is set to the [language].
	 */
	@Attribute("documentationProviderInstance", converter = InstanceConverter::class)
	var documentationProviderInstance: AdvancedSearchDocumentationProvider? = null
	
	/**
	 * **Optional**
	 *
	 * You may provide an instance of [AdvancedSearchInjectionProvider] in order to
	 * inject a language in parameters when needed.
	 *
	 * for example the [JavaAdvancedSearchInjectionProvider] injects java into parameters
	 * when a statement like `$class extends '<base-class>'` is found in the code.
	 */
	@Attribute("injectionProviderInstance", converter = InstanceConverter::class)
	var injectionProviderInstance: AdvancedSearchInjectionProvider? = null
	
	/* ---------------------------------------------------------------------------------------------------- */
	
	@Suppress("UNCHECKED_CAST")
	class Constructor<T : Any>(private val clazz: Class<T>) {
		fun new(vararg args: Any): T {
			val ctor = clazz.constructors.find {
				val types = it.parameterTypes
				if (types.size != args.size) {
					return@find false
				}
				
				args.forEachIndexed { i, arg ->
					if (!types[i].isInstance(arg)) {
						return@find false
					}
				}
				
				return@find true
			} ?: throw NoSuchMethodException()
			
			return ctor.newInstance(*args) as T
		}
	}
	
	private class ConstructorConverter : Converter<Constructor<out Any>>() {
		override fun toString(value: Constructor<out Any>): String? = null
		override fun fromString(value: String): Constructor<out Any>? {
			return try {
				Constructor(Class.forName(value))
			} catch (e: Exception) {
				null
			}
		}
	}
	
	private class InstanceConverter : Converter<Any>() {
		override fun toString(value: Any) = null
		override fun fromString(value: String): Any? {
			return try {
				val fieldName = value.substringAfterLast('.')
				val className = value.substring(0, value.length - fieldName.length - 1)
				Class.forName(className).getField(fieldName).get(null)
			} catch (e: Exception) {
				null
			}
		}
	}
}
