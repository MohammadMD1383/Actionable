package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile

inline fun PsiFile.elementAt(caret: Caret) = findElementAt(caret.offset)
