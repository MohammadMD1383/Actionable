package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.Converter
import com.intellij.util.xmlb.annotations.Attribute
import ir.mmd.intellijDev.Actionable.find.advanced.agent.java.JavaAdvancedSearchExtensionFactory
import ir.mmd.intellijDev.Actionable.find.advanced.agent.java.JavaAdvancedSearchInjectionProvider
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.util.WrappedException

val AdvancedSearchExtensionPoint = ExtensionPointName.create<AdvancedSearchProviderFactoryBean>("ir.mmd.intellijDev.Actionable.advancedSearch.providerFactory")

/**
 * for an example please explore the [ir.mmd.intellijDev.Actionable.find.advanced.agent.java] package.
 *
 * for an implementation example you may take a look at [JavaAdvancedSearchExtensionFactory]
 */
abstract class AdvancedSearchExtensionFactory {
	/**
	 * **Required**
	 *
	 * responsible for creating an agent class
	 *
	 * @throws WrappedException
	 */
	@Throws(WrappedException::class)
	abstract fun createAgent(project: Project, file: AdvancedSearchFile): AdvancedSearchAgent
	
	/**
	 * **Optional**
	 *
	 * You may return an instance implementing [AdvancedSearchCompletionProvider] to provide
	 * completions for the advanced search file when language property matches your extension.
	 *
	 * **Note**
	 *
	 * You may use singleton pattern to reduce the overhead of creating new classes each time.
	 *
	 * This method is called every time that user triggers the auto completion.
	 */
	open fun getCompletionProvider(project: Project): AdvancedSearchCompletionProvider? = null
	
	/**
	 * **Optional**
	 *
	 * You may return an instance of annotator to be used when the advanced search file's
	 * language property matches your extension.
	 *
	 * **Note**
	 *
	 * This method may called several times in a short period of time.
	 * avoid instantiating a new object each time, preferably use singleton pattern.
	 */
	open fun getAnnotator(project: Project): Annotator? = null
	
	/**
	 * **Optional**
	 *
	 * You may return an instance of [AdvancedSearchDocumentationProvider] in order to
	 * render documentation for top-level properties, variables, identifiers, property values
	 * and parameters when the file's language property matches your extension.
	 *
	 * **Note**
	 *
	 * this method is called every time the user looks for a symbol documentation.
	 * avoid instantiating new object each time to reduce to overhead. preferably use
	 * singleton pattern.
	 */
	open fun getDocumentationProvider(project: Project): AdvancedSearchDocumentationProvider? = null
	
	/**
	 * **Optional**
	 *
	 * You may return an instance of [AdvancedSearchInjectionProvider] in order to
	 * inject a language in parameters when needed.
	 *
	 * for example the [JavaAdvancedSearchInjectionProvider] injects java into parameters
	 * when a statement like `$class extends '<base-class>'` is found in the code.
	 *
	 * **Note**
	 *
	 * This method is called several times in a short period. avoid instantiating new
	 * object each time. preferably use singleton pattern.
	 */
	open fun getInjectionProvider(project: Project): AdvancedSearchInjectionProvider? = null
}

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
