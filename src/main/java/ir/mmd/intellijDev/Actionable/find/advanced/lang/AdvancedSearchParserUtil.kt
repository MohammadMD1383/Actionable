package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.PsiBuilder
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes

fun rBraceAsEOSInBody(builder: PsiBuilder, level: Int): Boolean {
	return builder.tokenType == AdvancedSearchTypes.RBRACE
}
