package ir.mmd.intellijDev.Actionable.ide

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.ui.showInputDialog
import ir.mmd.intellijDev.Actionable.vfs.MemoryMappedVirtualFile

open class CreateTemporaryFileAction : ActionBase(), DumbAware {
	protected open fun createVirtualFile(fileName: String): VirtualFile = MemoryMappedVirtualFile(fileName)
	
	context (LazyEventContext)
	override fun performAction() {
		val fileName = showInputDialog(
			project,
			"Create Temporary File",
		)?.ifBlank { "Temporary File" } ?: return
		
		FileEditorManager.getInstance(project).openTextEditor(
			OpenFileDescriptor(project, createVirtualFile(fileName)),
			true
		)
	}
}

class CreateSmartTemporaryFileAction : CreateTemporaryFileAction() {
	override fun createVirtualFile(fileName: String) = LightVirtualFile(fileName)
}
