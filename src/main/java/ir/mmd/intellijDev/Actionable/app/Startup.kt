package ir.mmd.intellijDev.Actionable.app

import com.intellij.openapi.application.PreloadingActivity
import ir.mmd.intellijDev.Actionable.text.macro.registerMacros

class Startup : PreloadingActivity() {
	override fun preload() {
		registerMacros()
	}
}
