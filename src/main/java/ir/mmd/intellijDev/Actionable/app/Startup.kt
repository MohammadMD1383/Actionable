package ir.mmd.intellijDev.Actionable.app

import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.progress.ProgressIndicator
import ir.mmd.intellijDev.Actionable.text.macro.registerMacros

class Startup : PreloadingActivity() {
	override fun preload() {
		registerMacros()
	}
	
	override fun preload(indicator: ProgressIndicator?) = preload()
}
