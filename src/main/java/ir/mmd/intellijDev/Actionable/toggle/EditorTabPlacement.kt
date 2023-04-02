package ir.mmd.intellijDev.Actionable.toggle

import com.intellij.ide.ui.UISettings
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import javax.swing.SwingConstants

abstract class ChangeEditorTabPlacementShortcut(private val placement: Int) : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() = service<UISettings>().run {
		editorTabPlacement = placement
		fireUISettingsChanged()
	}
}

class SetEditorTabPlacementToTop : ChangeEditorTabPlacementShortcut(SwingConstants.TOP)
class SetEditorTabPlacementToLeft : ChangeEditorTabPlacementShortcut(SwingConstants.LEFT)
class SetEditorTabPlacementToBottom : ChangeEditorTabPlacementShortcut(SwingConstants.BOTTOM)
class SetEditorTabPlacementToRight : ChangeEditorTabPlacementShortcut(SwingConstants.RIGHT)
