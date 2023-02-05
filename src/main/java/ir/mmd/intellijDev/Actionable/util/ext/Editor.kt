package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import ir.mmd.intellijDev.Actionable.caret.justification.JustifyCaretUtil
import ir.mmd.intellijDev.Actionable.util.DuplicateUtil

/**
 * shortcut for `editor.caretModel.caretCount`
 */
inline val Editor.caretCount: Int get() = caretModel.caretCount

/**
 * shortcut for `editor.caretModel.allCarets`
 */
inline val Editor.allCarets: List<Caret> get() = caretModel.allCarets

/**
 * shortcut for checking if all the carets have selection
 *
 * @see hasNotAnySelections
 */
inline val Editor.allCaretsHaveSelection: Boolean get() = caretModel.allCarets.all { it.hasSelection() }

/**
 * shortcut for checking if all carets do not have any selections
 *
 * @see allCaretsHaveSelection
 */
inline val Editor.hasNotAnySelections: Boolean get() = !selectionModel.hasSelection(true)

/**
 * Returns a [JustifyCaretUtil] associated with this [Editor]
 */
inline val Editor.justifier: JustifyCaretUtil get() = JustifyCaretUtil(this)

/**
 * Returns a [DuplicateUtil] associated with this [Editor]
 */
inline val Editor.duplicator: DuplicateUtil get() = DuplicateUtil(this)
