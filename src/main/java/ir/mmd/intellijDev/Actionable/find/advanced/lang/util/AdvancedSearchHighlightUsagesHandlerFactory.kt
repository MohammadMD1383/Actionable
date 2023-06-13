package ir.mmd.intellijDev.Actionable.find.advanced.lang.util

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.Consumer
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatements
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes

class AdvancedSearchHighlightUsagesHandlerFactory : HighlightUsagesHandlerFactory {
	private class HighlightUsagesHandler(editor: Editor, file: PsiFile) : HighlightUsagesHandlerBase<PsiElement>(editor, file) {
		override fun getTargets(): MutableList<PsiElement> {
			val list = mutableListOf<PsiElement>()
			
			myFile.findElementAt(myEditor.caretModel.primaryCaret.offset)?.let {
				if (it.elementType == AdvancedSearchTypes.VARIABLE) {
					list += it
				}
			}
			
			return list
		}
		
		override fun selectTargets(targets: MutableList<out PsiElement>, selectionConsumer: Consumer<in MutableList<out PsiElement>>) {
			selectionConsumer.consume(targets)
		}
		
		override fun computeUsages(targets: MutableList<out PsiElement>) {
			if (targets.isEmpty()) {
				return
			}
			
			val element = targets.first()
			val targetText = element.text
			element.parentOfType<AdvancedSearchPsiStatements>()
				?.statementList
				?.forEach {
					val psiVariable = it.psiVariable ?: return@forEach
					if (psiVariable.text == targetText) {
						myReadUsages.add(psiVariable.textRange)
					}
				}
		}
	}
	
	override fun createHighlightUsagesHandler(editor: Editor, file: PsiFile): HighlightUsagesHandlerBase<*>? {
		return if (file !is AdvancedSearchFile) null else HighlightUsagesHandler(editor, file)
	}
}
