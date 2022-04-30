package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Editor
import ir.mmd.intellijDev.Actionable.caret.justification.JustifyCaretUtil

inline val Editor.caretCount: Int get() = caretModel.caretCount
inline val Editor.hasNotAnySelections: Boolean get() = !selectionModel.hasSelection(true)
inline val Editor.justifier: JustifyCaretUtil get() = JustifyCaretUtil(this)
