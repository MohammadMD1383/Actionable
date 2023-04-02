package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.isFile

/**
 * Traverses the tree up to a directory.
 */
fun VirtualFile.getParentDirectory(withSelf: Boolean = false): VirtualFile? {
	var current: VirtualFile? = if (withSelf) this else parent
	
	while (current != null && current.isFile) {
		current = current.parent
	}
	
	return current
}
