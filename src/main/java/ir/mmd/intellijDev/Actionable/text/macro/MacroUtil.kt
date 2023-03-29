package ir.mmd.intellijDev.Actionable.text.macro

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.PathManager
import ir.mmd.intellijDev.Actionable.action.Actions
import ir.mmd.intellijDev.Actionable.action.registerMacro
import ir.mmd.intellijDev.Actionable.action.unregisterMacro
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

fun getMacroStorePath(): Path = Paths.get(PathManager.getConfigPath(), "Actionable/macro")

fun getMacroStoreDirectory(): File = getMacroStorePath().toFile()

fun getMacroFiles(): Array<File>? = getMacroStoreDirectory().run { mkdirs(); listFiles() }

fun getMacroNames(): List<String>? = getMacroFiles()?.map(File::getName)

fun registerMacros() {
	val actionManager = ActionManager.getInstance()
	getMacroFiles()?.forEach {
		actionManager.registerMacro(it.nameWithoutExtension, it.readText())
	}
}

fun unregisterMacros() {
	val actionManager = ActionManager.getInstance()
	actionManager.getActionIdList(Actions.MACRO_PREFIX).forEach(actionManager::unregisterMacro)
}

fun reRegisterMacros() {
	unregisterMacros()
	registerMacros()
}
