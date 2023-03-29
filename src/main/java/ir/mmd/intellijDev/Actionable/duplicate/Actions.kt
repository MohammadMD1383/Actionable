package ir.mmd.intellijDev.Actionable.duplicate

import ir.mmd.intellijDev.Actionable.util.DuplicateUtil

class DuplicateLinesUp : DuplicateAction(DuplicateUtil::duplicateUp)

class DuplicateLinesDown : DuplicateAction(DuplicateUtil::duplicateDown)
