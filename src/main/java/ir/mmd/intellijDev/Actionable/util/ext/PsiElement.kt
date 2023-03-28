package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import kotlin.reflect.KClass

/**
 * For Compatibility
 *
 * Returns the element type of the [PsiElement]
 */
val PsiElement.elementType: IElementType get() = PsiUtilCore.getElementType(this)

/**
 * For Compatibility
 *
 * Returns next leaf element
 */
@Suppress("NOTHING_TO_INLINE")
inline fun PsiElement.nextLeaf(skipEmptyElements: Boolean = false) = PsiTreeUtil.nextLeaf(this, skipEmptyElements)

/**
 * For Compatibility
 *
 * Returns previous leaf element
 */
@Suppress("NOTHING_TO_INLINE")
inline fun PsiElement.prevLeaf(skipEmptyElements: Boolean = false) = PsiTreeUtil.prevLeaf(this, skipEmptyElements)

/**
 * For Compatibility
 *
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
 * For Compatibility
 *
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
 * For Compatibility
 *
 * Returns parent elements of types [classes]
 */
fun <T : PsiElement> PsiElement.parentOfTypes(vararg classes: KClass<out T>, withSelf: Boolean = false): T? {
	val start = if (withSelf) this else this.parent
	return PsiTreeUtil.getNonStrictParentOfType(start, *classes.map { it.java }.toTypedArray())
}

/**
 * For Compatibility
 *
 * Returns parent element of type [T]
 */
inline fun <reified T : PsiElement> PsiElement.parentOfType(withSelf: Boolean = false): T? {
	return PsiTreeUtil.getParentOfType(this, T::class.java, !withSelf)
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
