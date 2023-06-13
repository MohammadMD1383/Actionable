package ir.mmd.intellijDev.Actionable.find.advanced.lang.util

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.DOUBLE_QUOTE
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.SINGLE_QUOTE

class AdvancedSearchQuoteHandler : SimpleTokenSetQuoteHandler(SINGLE_QUOTE, DOUBLE_QUOTE)
