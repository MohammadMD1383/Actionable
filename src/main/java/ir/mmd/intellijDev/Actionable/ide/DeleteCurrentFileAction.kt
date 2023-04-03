package ir.mmd.intellijDev.Actionable.ide

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.isFile
import com.intellij.util.application
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.ui.showConfirm

class DeleteCurrentFileAction : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() {
		if (showConfirm(project, "Delete Current File", "Are you sure to delete (${virtualFile.name})?")) {
			application.runWriteAction {
				virtualFile.delete(this)
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor and hasVirtualFile and virtualFile.isFile
}
