package ir.mmd.intellijDev.Actionable.psi

import com.intellij.psi.PsiParameter
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep

@Keep
class JavaDeleteParameterAtCaretAction : PsiAtCaretAction<PsiParameter>(PsiParameter::class) {
	override fun perform(element: PsiParameter) = element.delete()
}
