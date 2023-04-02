package ir.mmd.intellijDev.Actionable.ide

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.DumbAware
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.ui.showInputDialog
import ir.mmd.intellijDev.Actionable.vfs.MemoryMappedVirtualFile

class CreateTemporaryFileAction : ActionBase(), DumbAware {
	context (LazyEventContext)
	override fun performAction() {
		val fileName = showInputDialog(
			project,
			"Create Temporary File",
		).let { if (it.isNullOrBlank()) "Temporary File" else it }
		
		FileEditorManager.getInstance(project).openTextEditor(
			OpenFileDescriptor(project, MemoryMappedVirtualFile(fileName)),
			true
		)
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor
}
