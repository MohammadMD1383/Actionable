package ir.mmd.intellijDev.Actionable.extra.ide.keymap

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.keymap.impl.ui.KeymapPanel
import com.intellij.openapi.options.ShowSettingsUtil

class OpenKeymapSettingsAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		ShowSettingsUtil.getInstance().showSettingsDialog(e.project, KeymapPanel::class.java)
	}
	
	override fun isDumbAware() = true
}
