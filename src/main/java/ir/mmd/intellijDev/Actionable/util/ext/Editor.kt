package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import ir.mmd.intellijDev.Actionable.caret.justification.JustifyCaretUtil
import ir.mmd.intellijDev.Actionable.util.DuplicateUtil

inline val Editor.caretCount: Int get() = caretModel.caretCount
inline val Editor.allCarets: List<Caret> get() = caretModel.allCarets
inline val Editor.allCaretsHasSelection: Boolean get() = caretModel.allCarets.all { it.hasSelection() }
inline val Editor.hasNotAnySelections: Boolean get() = !selectionModel.hasSelection(true)
inline val Editor.justifier: JustifyCaretUtil get() = JustifyCaretUtil(this)
inline val Editor.duplicator: DuplicateUtil get() = DuplicateUtil(this)
