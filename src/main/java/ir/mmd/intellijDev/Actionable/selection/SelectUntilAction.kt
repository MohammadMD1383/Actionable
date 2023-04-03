package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.CaretUtil
import ir.mmd.intellijDev.Actionable.util.afterDoing
import ir.mmd.intellijDev.Actionable.util.ext.removeCharAt
import ir.mmd.intellijDev.Actionable.util.ext.util
import ir.mmd.intellijDev.Actionable.util.ext.withEachMapped
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

abstract class SelectUntilAction(private val dir: Int) : ActionBase(), DumbAware {
	private var lastKeyAdapter: KeyAdapter? = null
	
	context (LazyEventContext)
	override fun performAction() {
		if (lastKeyAdapter != null) return afterDoing {
			editor.contentComponent.removeKeyListener(lastKeyAdapter)
			lastKeyAdapter = null
		}
		
		editor.contentComponent.addKeyListener(object : KeyAdapter() {
			override fun keyTyped(e: KeyEvent) {
				if (e.keyChar != KeyEvent.CHAR_UNDEFINED) {
					runWriteCommandAction {
						allCarets.withEachMapped({ it.util }) {
							document.removeCharAt(offset - 1)
							if (!moveUntilReached(e.keyChar.toString(), "\n", dir)) {
								offset += dir
							}
							
							makeOffsetDiffSelection()
							commit()
						}
					}
				}
				
				editor.contentComponent.removeKeyListener(this)
				lastKeyAdapter = null
			}
		}.also { lastKeyAdapter = it })
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}

class ForwardSelectUntilAction : SelectUntilAction(CaretUtil.FORWARD)
class BackwardSelectUntilAction : SelectUntilAction(CaretUtil.BACKWARD)
