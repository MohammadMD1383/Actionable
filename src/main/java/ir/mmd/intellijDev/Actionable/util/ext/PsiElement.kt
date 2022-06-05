package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import kotlin.reflect.KClass

inline fun PsiElement.nextLeaf(skipEmptyElements: Boolean = false) = PsiTreeUtil.nextLeaf(this, skipEmptyElements)
inline fun PsiElement.prevLeaf(skipEmptyElements: Boolean = false) = PsiTreeUtil.prevLeaf(this, skipEmptyElements)
inline fun PsiElement.acceptChildren(crossinline block: (PsiElement) -> Unit) = acceptChildren(psiElementVisitor(block))

fun PsiElement.prevLeafNoWhitespace(skipEmptyElements: Boolean = false): PsiElement? {
	var element = prevLeaf(skipEmptyElements)
	while (element != null && element is PsiWhiteSpace) {
		element = element.prevLeaf(skipEmptyElements)
	}
	return element
}

fun PsiElement.nextLeafNoWhitespace(skipEmptyElements: Boolean = false): PsiElement? {
	var element = nextLeaf(skipEmptyElements)
	while (element != null && element is PsiWhiteSpace) {
		element = element.nextLeaf(skipEmptyElements)
	}
	return element
}

fun <T : PsiElement> PsiElement.parentOfTypes(vararg classes: KClass<out T>, withSelf: Boolean = false): T? {
	val start = if (withSelf) this else this.parent
	return PsiTreeUtil.getNonStrictParentOfType(start, *classes.map { it.java }.toTypedArray())
}

inline fun <reified T : PsiElement> PsiElement.parentOfType(withSelf: Boolean = false): T? {
	return PsiTreeUtil.getParentOfType(this, T::class.java, !withSelf)
}

inline fun psiElementVisitor(crossinline block: (PsiElement) -> Unit) = object : PsiElementVisitor() {
	override fun visitElement(element: PsiElement) = block(element)
}
