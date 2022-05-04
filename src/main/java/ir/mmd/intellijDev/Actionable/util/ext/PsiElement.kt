package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

inline fun PsiElement.nextLeaf(skipEmptyElements: Boolean = false) = PsiTreeUtil.nextLeaf(this, skipEmptyElements)
inline fun PsiElement.prevLeaf(skipEmptyElements: Boolean = false) = PsiTreeUtil.prevLeaf(this, skipEmptyElements)
