package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile

inline fun PsiFile.elementAt(caret: Caret) = findElementAt(caret.offset)
inline fun PsiFile.elementAtOrBefore(caret: Caret) = caret.offset.let { findElementAt(it) ?: findElementAt(it - 1) }
