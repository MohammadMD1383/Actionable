package ir.mmd.intellijDev.Actionable.find.advanced.lang.util

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.LBRACE
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.RBRACE

class AdvancedSearchBraceMatcher : PairedBraceMatcher {
	override fun getPairs() = arrayOf(
		BracePair(LBRACE, RBRACE, true)
	)
	
	override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?) = true
	
	override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
		return file.findElementAt(openingBraceOffset)
			?.parentOfType<AdvancedSearchPsiStatement>()
			?.textOffset ?: openingBraceOffset
	}
}
