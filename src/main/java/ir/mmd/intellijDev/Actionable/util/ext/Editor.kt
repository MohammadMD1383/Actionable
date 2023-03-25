package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import ir.mmd.intellijDev.Actionable.caret.justification.JustifyCaretUtil
import ir.mmd.intellijDev.Actionable.util.DuplicateUtil

/**
 * Shortcut for `editor.caretModel.allCarets`
 */
inline val Editor.allCarets: List<Caret> get() = caretModel.allCarets

/**
 * Returns a [JustifyCaretUtil] associated with this [Editor]
 */
inline val Editor.justifier: JustifyCaretUtil get() = JustifyCaretUtil(this)

/**
 * Returns a [DuplicateUtil] associated with this [Editor]
 */
inline val Editor.duplicator: DuplicateUtil get() = DuplicateUtil(this)
