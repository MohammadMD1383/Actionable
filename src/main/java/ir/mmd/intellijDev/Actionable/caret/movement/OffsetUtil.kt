package ir.mmd.intellijDev.Actionable.caret.movement

import com.intellij.openapi.editor.Document
import ir.mmd.intellijDev.Actionable.util.charAtOrNull
import ir.mmd.intellijDev.Actionable.util.contains

fun Document.findOffsetOfNext(fromOffset: Int, chars: String, hardStops: String, step: Int): Int { // todo rewrite with iterator (charsSequence.iterator())
	var newOffset = fromOffset
	while (true) {
		if ((charAtOrNull(newOffset) ?: return newOffset) in chars + hardStops) return newOffset
		newOffset += step
	}
}
