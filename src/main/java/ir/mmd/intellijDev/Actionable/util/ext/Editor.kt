package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor

/**
 * Shortcut for `editor.caretModel.allCarets`
 */
inline val Editor.allCarets: List<Caret> get() = caretModel.allCarets
