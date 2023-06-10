package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.codeInsight.editorActions.moveLeftRight.MoveElementLeftRightHandler
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiParameters

class AdvancedSearchMoveElementLeftRightHandler : MoveElementLeftRightHandler() {
	override fun getMovableSubElements(element: PsiElement): Array<PsiElement> {
		return if (element !is AdvancedSearchPsiParameters) emptyArray() else element.parameterList.toTypedArray()
	}
}
