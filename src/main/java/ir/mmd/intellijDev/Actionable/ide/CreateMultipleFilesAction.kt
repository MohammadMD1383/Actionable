package ir.mmd.intellijDev.Actionable.ide

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.findOrCreateDirectory
import com.intellij.util.application
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.ui.showMultilineInputDialog
import ir.mmd.intellijDev.Actionable.util.ext.getParentDirectory
import kotlin.io.path.Path

class CreateMultipleFilesAction : ActionBase(), DumbAware {
	context(LazyEventContext)
	override fun performAction() {
		val directory = virtualFile.getParentDirectory(true) ?: return
		val files = (showMultilineInputDialog(project, "Create Multiple Files", 10, 200) ?: return)
			.split('\n').filter { it.isNotBlank() }.distinct()
		
		application.runWriteAction {
			files.forEach {
				if ('/' in it) {
					val path = Path(it)
					directory.findOrCreateDirectory(path.parent.toString())
						.findOrCreateChildData(this, path.fileName.toString())
				} else {
					directory.findOrCreateChildData(this, it)
				}
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasVirtualFile
}
