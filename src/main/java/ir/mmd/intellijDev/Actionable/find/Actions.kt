package ir.mmd.intellijDev.Actionable.find

import ir.mmd.intellijDev.Actionable.internal.proguard.Keep

@Keep
class SelectPreviousOccurrence : FindAction(false)

@Keep
class SelectNextOccurrence : FindAction(true)
