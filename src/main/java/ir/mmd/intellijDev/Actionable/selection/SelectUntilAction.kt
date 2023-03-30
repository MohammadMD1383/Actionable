package ir.mmd.intellijDev.Actionable.selection

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.CaretUtil
import ir.mmd.intellijDev.Actionable.util.afterDoing
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.removeCharAt
import ir.mmd.intellijDev.Actionable.util.ext.util
import ir.mmd.intellijDev.Actionable.util.ext.withEachMapped
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class SelectUntilAction : AnAction() {
	private var lastKeyAdapter: KeyAdapter? = null
	
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
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
							if (!moveUntilReached(e.keyChar.toString(), "\n", CaretUtil.FORWARD)) {
								offset++
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
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun isDumbAware() = true
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
