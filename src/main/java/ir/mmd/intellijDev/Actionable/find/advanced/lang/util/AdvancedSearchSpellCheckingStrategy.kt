package ir.mmd.intellijDev.Actionable.find.advanced.lang.util

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy

class AdvancedSearchSpellCheckingStrategy : SpellcheckingStrategy() {
	override fun isMyContext(element: PsiElement): Boolean {
		return element is PsiComment
	}
}
