package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace

/**
 * shortcut for `PsiFile.findElementAt(caret.offset)`
 */
@Suppress("NOTHING_TO_INLINE")
inline fun PsiFile.elementAt(caret: Caret) = findElementAt(caret.offset)

/**
 * Tries to find element at [Caret.getOffset] or [Caret.getOffset]` - 1` if the first attempt result was `null`
 */
@Suppress("NOTHING_TO_INLINE")
inline fun PsiFile.elementAtOrBefore(
	caret: Caret,
	skipWhitespace: Boolean = true,
	skipEmptyElements: Boolean = true
) = caret.offset.let { o ->
	(findElementAt(o) ?: findElementAt(o - 1))?.let {
		if (it is PsiWhiteSpace) it.prevLeafNoWhitespace(skipEmptyElements) else it
	}
}
