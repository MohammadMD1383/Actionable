package ir.mmd.intellijDev.Actionable.duplicate

import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.DuplicateUtil

@Keep
class DuplicateLinesUp : DuplicateAction(DuplicateUtil::duplicateUp)

@Keep
class DuplicateLinesDown : DuplicateAction(DuplicateUtil::duplicateDown)
