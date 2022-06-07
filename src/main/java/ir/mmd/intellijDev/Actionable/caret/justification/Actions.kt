package ir.mmd.intellijDev.Actionable.caret.justification

import ir.mmd.intellijDev.Actionable.internal.proguard.Keep


@Keep
class JustifyCaretsEndAndShift : JustifyAction(JustifyCaretUtil::justifyCaretsEndWithShifting)

@Keep
class JustifyCaretsStart : JustifyAction(JustifyCaretUtil::justifyCaretsStart)

@Keep
class JustifyCaretsEnd : JustifyAction(JustifyCaretUtil::justifyCaretsEnd)
