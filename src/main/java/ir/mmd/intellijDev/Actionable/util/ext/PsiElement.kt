package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.nextLeaf
import com.intellij.psi.util.prevLeaf

/**
 * Returns next leaf element but not a [PsiWhiteSpace]
 */
fun PsiElement.nextLeafNoWhitespace(skipEmptyElements: Boolean = false): PsiElement? {
	var element = nextLeaf(skipEmptyElements)
	while (element != null && element is PsiWhiteSpace) {
		element = element.nextLeaf(skipEmptyElements)
	}
	return element
}

/**
 * Returns previous leaf element but not a [PsiWhiteSpace]
 */
fun PsiElement.prevLeafNoWhitespace(skipEmptyElements: Boolean = false): PsiElement? {
	var element = prevLeaf(skipEmptyElements)
	while (element != null && element is PsiWhiteSpace) {
		element = element.prevLeaf(skipEmptyElements)
	}
	return element
}

/**
 * Returns parent element but not a [PsiWhiteSpace]
 */
inline val PsiElement.parentNoWhitespace: PsiElement?
	get() {
		var parent: PsiElement? = parent
		while (parent is PsiWhiteSpace) {
			parent = parent.parent
		}
		
		return parent
	}

/**
 * Checks whether that this [PsiElement] exists inside [other] [PsiElement]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun PsiElement.contains(other: PsiElement): Boolean {
	val thisRange = textRange
	val otherRange = other.textRange
	
	return thisRange.startOffset <= otherRange.startOffset && thisRange.endOffset >= otherRange.endOffset
}
