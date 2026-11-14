package ir.mmd.intellijDev.Actionable.app

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import ir.mmd.intellijDev.Actionable.text.macro.registerMacros
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Registers user macros once per IDE lifetime.
 *
 * Replaces the removed [com.intellij.openapi.application.PreloadingActivity].
 */
class Startup : ProjectActivity {
	companion object {
		private val registered = AtomicBoolean(false)
	}

	override suspend fun execute(project: Project) {
		if (registered.compareAndSet(false, true)) {
			registerMacros()
		}
	}
}
