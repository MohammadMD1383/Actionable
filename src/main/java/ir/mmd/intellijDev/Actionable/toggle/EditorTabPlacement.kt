package ir.mmd.intellijDev.Actionable.toggle

import com.intellij.ide.ui.UISettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.hasProject
import ir.mmd.intellijDev.Actionable.util.service
import javax.swing.SwingConstants

abstract class ChangeEditorTabPlacementShortcut(private val placement: Int) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = service<UISettings>().run {
		editorTabPlacement = placement
		fireUISettingsChanged()
	}
	
	override fun isDumbAware() = true
	override fun update(e: AnActionEvent) = e.enableIf { hasProject }
}

@Keep
class SetEditorTabPlacementToTop : ChangeEditorTabPlacementShortcut(SwingConstants.TOP)

@Keep
class SetEditorTabPlacementToLeft : ChangeEditorTabPlacementShortcut(SwingConstants.LEFT)

@Keep
class SetEditorTabPlacementToBottom : ChangeEditorTabPlacementShortcut(SwingConstants.BOTTOM)

@Keep
class SetEditorTabPlacementToRight : ChangeEditorTabPlacementShortcut(SwingConstants.RIGHT)
