package ir.mmd.intellijDev.Actionable.toggle

import com.intellij.ide.ui.UISettings
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import ir.mmd.intellijDev.Actionable.util.ext.enableIf

import javax.swing.SwingConstants

abstract class ChangeEditorTabPlacementShortcut(private val placement: Int) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = service<UISettings>().run {
		editorTabPlacement = placement
		fireUISettingsChanged()
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

class SetEditorTabPlacementToTop : ChangeEditorTabPlacementShortcut(SwingConstants.TOP)

class SetEditorTabPlacementToLeft : ChangeEditorTabPlacementShortcut(SwingConstants.LEFT)

class SetEditorTabPlacementToBottom : ChangeEditorTabPlacementShortcut(SwingConstants.BOTTOM)

class SetEditorTabPlacementToRight : ChangeEditorTabPlacementShortcut(SwingConstants.RIGHT)
