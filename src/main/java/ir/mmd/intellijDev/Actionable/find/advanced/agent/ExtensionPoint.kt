package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.util.xmlb.Converter
import com.intellij.util.xmlb.annotations.Attribute

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
	@Attribute("agentClass", converter = AgentConverter::class)
	lateinit var agentClass: Constructor<AdvancedSearchAgent>
	
	/**
	 * **Optional**
	 *
	 * You may provide a class **instance** implementing [AdvancedSearchCompletionProvider]
	 * to provide completions for the advanced search file when language property is set to [language].
	 */
	@Attribute("completionProviderInstance", converter = CompletionProviderConverter::class)
	var completionProviderInstance: AdvancedSearchCompletionProvider? = null
	
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
	
	@Suppress("UNCHECKED_CAST")
	private abstract class ConstructorConverter<T : Any> : Converter<Constructor<T>>() {
		override fun toString(value: Constructor<T>): String? = null
		override fun fromString(value: String): Constructor<T> {
			return Constructor(Class.forName(value) as Class<T>)
		}
	}
	
	@Suppress("UNCHECKED_CAST")
	private abstract class InstanceConverter<T : Any> : Converter<T>() {
		override fun toString(value: T) = null
		override fun fromString(value: String): T? {
			val fieldName = value.substringAfterLast('.')
			val className = value.substring(0, value.length - fieldName.length - 1)
			
			return try {
				Class.forName(className).getField(fieldName).get(null) as T
			} catch (e: Exception) {
				null
			}
		}
	}
	
	private class AgentConverter : ConstructorConverter<AdvancedSearchAgent>()
	private class CompletionProviderConverter : InstanceConverter<AdvancedSearchCompletionProvider>()
}
