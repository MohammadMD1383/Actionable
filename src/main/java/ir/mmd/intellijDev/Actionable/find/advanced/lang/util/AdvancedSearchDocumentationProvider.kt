package ir.mmd.intellijDev.Actionable.find.advanced.lang.util

import com.intellij.icons.AllIcons
import com.intellij.model.Pointer
import com.intellij.platform.backend.documentation.DocumentationResult
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.DocumentationTargetProvider
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentOfTypes
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchContext
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findExtensionFor
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.*
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchLightPsiElement.ElementType

class AdvancedSearchDocumentationProvider : DocumentationTargetProvider, PsiDocumentationTargetProvider {
	override fun documentationTargets(file: PsiFile, offset: Int): List<DocumentationTarget> {
		if (file !is AdvancedSearchFile) {
			return emptyList()
		}
		
		val element = file.findElementAt(offset) ?: return emptyList()
		val language = file.properties?.languagePsiProperty?.value ?: return emptyList()
		val type: ElementType
		val symbol: String
		
		when (element.elementType) {
			AdvancedSearchTypes.VARIABLE -> {
				type = ElementType.Variable
				symbol = element.text
			}
			
			AdvancedSearchTypes.IDENTIFIER -> {
				symbol = element.text
				type = if (element.parentOfType<AdvancedSearchPsiTopLevelProperty>() != null) {
					if (symbol == "language") {
						return listOf(AdvancedSearchLanguagePropertyDocumentationTarget)
					}
					
					ElementType.Property
				} else {
					ElementType.Identifier
				}
			}
			
			AdvancedSearchTypes.STRING_SEQ,
			AdvancedSearchTypes.STRING_ESCAPE_SEQ -> {
				val parent = element.parentOfTypes(AdvancedSearchPsiTopLevelProperty::class, AdvancedSearchPsiParameter::class) ?: return emptyList()
				symbol = element.parentOfType<AdvancedSearchPsiStringLiteral>()!!.content
				type = if (parent is AdvancedSearchPsiTopLevelProperty) ElementType.Value else ElementType.Parameter
			}
			
			else -> return emptyList()
		}
		
		return listOf(AdvancedSearchDocumentationTarget(element, language, symbol, type))
	}
	
	override fun documentationTarget(element: PsiElement, originalElement: PsiElement?): DocumentationTarget? {
		if (element !is AdvancedSearchLightPsiElement) {
			return null
		}
		
		if (element.type == ElementType.Property && element.toString() == "language") {
			return AdvancedSearchLanguagePropertyDocumentationTarget
		}
		
		originalElement ?: return null
		val language = (originalElement.containingFile as AdvancedSearchFile?)?.properties?.languagePsiProperty?.value ?: return null
		return AdvancedSearchDocumentationTarget(originalElement, language, element.toString(), element.type)
	}
}

@Suppress("UnstableApiUsage")
open class AdvancedSearchDocumentationTarget(
	private val element: PsiElement,
	private val language: String,
	private val symbol: String,
	private val type: ElementType
) : DocumentationTarget {
	override fun computePresentation(): TargetPresentation {
		val icon = when (type) {
			ElementType.Variable -> AllIcons.Nodes.Type
			ElementType.Property -> AllIcons.Nodes.Property
			ElementType.Identifier -> AllIcons.Nodes.Variable
			ElementType.Parameter -> AllIcons.Nodes.Parameter
			ElementType.Value -> AllIcons.Debugger.Value
		}
		
		return TargetPresentation
			.builder(symbol)
			.icon(icon)
			.presentation()
	}
	
	override fun computeDocumentation(): DocumentationResult? {
		val documentationProvider = AdvancedSearchExtensionPoint.findExtensionFor(language)
			?.getDocumentationProvider(element.project) ?: return null
		val docText = when (type) {
			ElementType.Property -> documentationProvider.getPropertyDocumentation(symbol)
			ElementType.Value -> documentationProvider.getPropertyValueDocumentation(element.parentOfType<AdvancedSearchPsiTopLevelProperty>()!!.key, symbol)
			ElementType.Variable -> documentationProvider.getVariableDocumentation(AdvancedSearchContext(element), symbol)
			ElementType.Identifier -> documentationProvider.getIdentifierDocumentation(AdvancedSearchContext(element), symbol)
			ElementType.Parameter -> documentationProvider.getParameterDocumentation(AdvancedSearchContext(element), symbol)
		} ?: return null
		
		return DocumentationResult.documentation(docText)
	}
	
	override fun createPointer(): Pointer<out DocumentationTarget> {
		return Pointer { this }
	}
}

@Suppress("UnstableApiUsage")
object AdvancedSearchLanguagePropertyDocumentationTarget : DocumentationTarget {
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
