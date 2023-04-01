package ir.mmd.intellijDev.Actionable.vfs

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileSystem
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

object MemoryMappedVirtualFileSystem : VirtualFileSystem() {
	override fun getProtocol() = "mmvf"
	override fun findFileByPath(path: String) = null
	override fun refresh(asynchronous: Boolean) = Unit
	override fun refreshAndFindFileByPath(path: String) = null
	override fun addVirtualFileListener(listener: VirtualFileListener) = Unit
	override fun removeVirtualFileListener(listener: VirtualFileListener) = Unit
	override fun deleteFile(requestor: Any?, vFile: VirtualFile) = Unit
	override fun moveFile(requestor: Any?, vFile: VirtualFile, newParent: VirtualFile) = Unit
	override fun renameFile(requestor: Any?, vFile: VirtualFile, newName: String) = Unit
	override fun createChildFile(requestor: Any?, vDir: VirtualFile, fileName: String) = throw IOException()
	override fun createChildDirectory(requestor: Any?, vDir: VirtualFile, dirName: String) = throw IOException()
	override fun copyFile(requestor: Any?, virtualFile: VirtualFile, newParent: VirtualFile, copyName: String) = throw IOException()
	override fun isReadOnly() = true
}

object MemoryMappedVirtualFileRoot : VirtualFile() {
	override fun getName() = "MMVF_ROOT"
	override fun getFileSystem() = MemoryMappedVirtualFileSystem
	override fun getPath() = "mmvf:///"
	override fun isWritable() = false
	override fun isDirectory() = true
	override fun isValid() = true
	override fun getParent() = null
	override fun getChildren(): Array<VirtualFile> = arrayOf()
	override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long) = throw IOException()
	override fun getInputStream() = throw IOException()
	override fun contentsToByteArray() = throw IOException()
	override fun getTimeStamp() = System.currentTimeMillis()
	override fun getLength() = 0L
	override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) = Unit
}

class MemoryMappedVirtualFile(
	private val name: String,
	private val contents: String = "",
	private val fileType: FileType? = null
) : VirtualFile() {
	private val uuid = UUID.randomUUID()
	
	override fun getName() = name
	override fun getFileSystem() = MemoryMappedVirtualFileSystem
	override fun getPath() = "mmvf:///$uuid/$name"
	override fun isWritable() = true
	override fun isDirectory() = false
	override fun isValid() = true
	override fun getParent() = MemoryMappedVirtualFileRoot
	override fun getChildren(): Array<VirtualFile> = arrayOf()
	override fun contentsToByteArray() = contents.toByteArray()
	override fun getTimeStamp() = System.currentTimeMillis()
	override fun getModificationStamp() = 0L
	override fun getLength() = contents.length.toLong()
	override fun getFileType(): FileType = fileType ?: super.getFileType().let { if (it is UnknownFileType) PlainTextFileType.INSTANCE else it }
	override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) = Unit
	override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long) = OutputStream.nullOutputStream()!!
	override fun getInputStream() = InputStream.nullInputStream()!!
}
