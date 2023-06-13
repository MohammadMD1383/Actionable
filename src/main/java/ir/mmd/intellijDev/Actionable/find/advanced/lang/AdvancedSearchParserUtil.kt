package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase.Parser
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes

typealias ParserFunction = (builder: PsiBuilder, level: Int) -> Boolean

fun rBraceAsEOSInBody(builder: PsiBuilder, level: Int): Boolean {
	return builder.tokenType == AdvancedSearchTypes.RBRACE
}

fun statement(
	builder: PsiBuilder, level: Int,
	variable: Parser, identifier: Parser, parameters: ParserFunction
): Boolean {
	if (variable.parse(builder, level)) {
		identifier.parse(builder, level)
		parameters(builder, level)
	} else {
		if (!identifier.parse(builder, level)) {
			return false
		}
		
		parameters(builder, level)
	}
	
	return true
}
