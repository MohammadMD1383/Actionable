package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Caret
import ir.mmd.intellijDev.Actionable.util.CaretUtil

inline val Caret.selectionRange: IntRange get() = selectionStart..selectionEnd
inline val Caret.util: CaretUtil get() = CaretUtil(this)

inline fun Caret.setSelection(range: IntRange) = setSelection(range.first, range.last)
