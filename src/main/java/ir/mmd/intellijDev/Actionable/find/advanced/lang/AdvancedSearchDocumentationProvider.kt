package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.icons.AllIcons
import com.intellij.model.Pointer
import com.intellij.navigation.TargetPresentation
import com.intellij.platform.backend.documentation.DocumentationResult
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.DocumentationTargetProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiTopLevelProperty
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes

class AdvancedSearchDocumentationProvider : DocumentationTargetProvider {
	override fun documentationTargets(file: PsiFile, offset: Int): List<DocumentationTarget> {
		if (file !is AdvancedSearchFile) {
			return emptyList()
		}
		
		val element = file.findElementAt(offset) ?: return emptyList()
		val language = file.properties?.languagePsiProperty?.value ?: return emptyList()
		val symbol = when (element.elementType) {
			AdvancedSearchTypes.VARIABLE -> element.text
			
			AdvancedSearchTypes.IDENTIFIER -> {
				val text = element.text
				if (element.parentOfType<AdvancedSearchPsiTopLevelProperty>() != null) {
					if (text == "language") {
						return listOf(AdvancedSearchLanguagePropertyDocumentationTarget())
					} else {
						"#$text"
					}
				} else {
					text
				}
			}
			
			else -> return emptyList()
		}
		
		return listOf(AdvancedSearchDocumentationTarget(file, language, symbol))
	}
}

open class AdvancedSearchDocumentationTarget(
	private val file: AdvancedSearchFile,
	private val language: String,
	private val symbol: String
) : DocumentationTarget {
	override fun computePresentation(): TargetPresentation {
		val icon = when {
			symbol[0] == '$' -> AllIcons.Nodes.Type
			symbol[0] == '#' -> AllIcons.Nodes.Property
			else -> AllIcons.Nodes.Variable
		}
		
		return TargetPresentation
			.builder(symbol.replace("#", ""))
			.icon(icon)
			.presentation()
	}
	
	override fun computeDocumentation(): DocumentationResult? {
		val docText = AdvancedSearchExtensionPoint.extensionList.find { it.language.equals(language, ignoreCase = true) }
			?.documentationProviderInstance
			?.getDocumentationFor(symbol) ?: return null
		
		return DocumentationResult.documentation(docText)
	}
	
	override fun createPointer(): Pointer<out DocumentationTarget> {
		return Pointer {
			if (file.properties?.languagePsiProperty?.value == language) this else null
		}
	}
}

class AdvancedSearchLanguagePropertyDocumentationTarget : DocumentationTarget {
	override fun computePresentation(): TargetPresentation {
		return TargetPresentation
			.builder("language")
			.icon(AllIcons.Nodes.Property)
			.presentation()
	}
	
	override fun computeDocumentation(): DocumentationResult {
		return DocumentationResult.documentation("""
			The language property is used to denote that this search should be done in which language.<br><br>
			Use autocompletion to see which languages are supported.<br>
			The language property is <b>case-insensitive</b>.
		""".trimIndent())
	}
	
	override fun createPointer(): Pointer<out DocumentationTarget> {
		return Pointer.hardPointer(this)
	}
}
