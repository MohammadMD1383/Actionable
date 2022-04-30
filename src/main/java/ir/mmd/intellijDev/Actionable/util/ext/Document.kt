package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.util.countRepeats

inline fun Document.charAtOrNull(offset: Int) = charsSequence.getOrNull(offset)

fun Document.findOffsetOfNext(
	startOffset: Int,
	chars: String,
	dir: Int
) = countRepeats(startOffset, dir) { (charAtOrNull(it) ?: return@countRepeats false) !in chars } + dir
