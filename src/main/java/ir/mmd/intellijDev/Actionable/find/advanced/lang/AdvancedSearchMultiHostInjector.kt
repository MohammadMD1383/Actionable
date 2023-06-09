package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchExtensionPoint
import ir.mmd.intellijDev.Actionable.find.advanced.agent.findExtensionFor
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStringLiteral
import org.intellij.lang.regexp.RegExpLanguage

private fun innerStringLiteralRangeOf(element: PsiElement): TextRange {
	return TextRange.from(1, element.text.lastIndex - 1)
}

class AdvancedSearchMultiHostInjector : MultiHostInjector {
	override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
		var e: AdvancedSearchPsiStatement? = context.parentOfType<AdvancedSearchPsiStatement>() ?: return
		val language = (context.containingFile as AdvancedSearchFile).properties?.languagePsiProperty?.value ?: return
		val variable = e!!.variable
		val identifier = e.identifier!!
		val parents = mutableListOf<String>()
		e = e.parentOfType<AdvancedSearchPsiStatement>()
		while (e != null) {
			parents.add(e.variable ?: e.identifier ?: "")
			e = e.parentOfType<AdvancedSearchPsiStatement>()
		}
		
		if (identifier.matches(".+-matches".toRegex())) {
			registrar.startInjecting(RegExpLanguage.INSTANCE)
				.addPlace(
					null, null,
					context as PsiLanguageInjectionHost,
					innerStringLiteralRangeOf(context)
				).doneInjecting()
			
			return
		}
		
		AdvancedSearchExtensionPoint.findExtensionFor(language)
			?.injectionProviderInstance
			?.getInjectionFor(context.project, variable, identifier, parents)
			?.let {
				registrar.startInjecting(it.language)
					.addPlace(
						it.prefix, it.suffix,
						context as PsiLanguageInjectionHost,
						innerStringLiteralRangeOf(context)
					).doneInjecting()
			}
	}
	
	override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
		return mutableListOf(AdvancedSearchPsiStringLiteral::class.java)
	}
}

// private fun MultiHostRegistrar.getInjectedFile(): PsiFile? {
// 	return try {
// 		val result = javaClass.getDeclaredMethod("getInjectedResult").run {
// 			isAccessible = true
// 			invoke(this@getInjectedFile)
// 		}
// 		val files = result.javaClass.getDeclaredField("files").get(result) as List<*>
// 		files[0] as PsiFile
// 	} catch (ignored: Exception) {
// 		null
// 	}
// }
