package ir.mmd.intellijDev.Actionable.text.macro.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.util.elementType

class MacroTemplateTranspiler(project: Project, macro: String, private val resolve: (placeholder: String) -> String) {
	private val macroFile = MacroTemplatePsiFactory.createFileFromText(project, macro)
	private val resolvedPlaceholders = mutableMapOf<String, String>()
	private lateinit var result: String
	var finalCaretOffset = 0
		private set
	
	private fun getReplacement(placeholder: String): String {
		return resolvedPlaceholders[placeholder] ?: resolve(placeholder).also { resolvedPlaceholders[placeholder] = it }
	}
	
	fun compute() {
		if (::result.isInitialized) {
			throw UnsupportedOperationException("macro is already computed")
		}
		
		result = macroFile.children.joinToString(separator = "") {
			when {
				it is MacroTemplatePsiPlaceholder -> getReplacement(it.placeholderName.text)
				it.elementType == MacroTemplateTypes.ESCAPED_ESCAPE -> "\\"
				it.elementType == MacroTemplateTypes.ESCAPED_DOLLAR -> "\$"
				else -> it.text
			}
		}
		
		result = """\$0\$""".toRegex().replace(result) {
			finalCaretOffset = it.range.first
			""
		}
	}
	
	fun getText() = result
}
