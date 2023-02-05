package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile

/**
 * shortcut for `PsiFile.findElementAt(caret.offset)`
 */
@Suppress("NOTHING_TO_INLINE")
inline fun PsiFile.elementAt(caret: Caret) = findElementAt(caret.offset)

/**
 * Tries to find element at [Caret.getOffset] or [Caret.getOffset]` - 1` if the first attempt result was `null`
 */
@Suppress("NOTHING_TO_INLINE")
inline fun PsiFile.elementAtOrBefore(caret: Caret) = caret.offset.let { findElementAt(it) ?: findElementAt(it - 1) }
