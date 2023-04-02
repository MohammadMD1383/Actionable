package ir.mmd.intellijDev.Actionable.ide

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.ui.showInputDialog
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.vfs.MemoryMappedVirtualFile

class CreateTemporaryFileAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent): Unit = (LazyEventContext(e)) {
		val fileName = showInputDialog(
			project,
			"Create Temporary File",
		).let { if (it.isNullOrBlank()) "Temporary File" else it }
		
		FileEditorManager.getInstance(project).openTextEditor(
			OpenFileDescriptor(project, MemoryMappedVirtualFile(fileName)),
			true
		)
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasProject and hasEditor }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
	override fun isDumbAware() = true
}
