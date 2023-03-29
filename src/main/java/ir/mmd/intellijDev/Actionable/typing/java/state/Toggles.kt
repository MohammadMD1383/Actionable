package ir.mmd.intellijDev.Actionable.typing.java.state

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class AutoClassCaseState : ProjectStateToggleAction<State>(State::class.java) {
	override fun State.get() = autoClassCaseEnabled
	override fun State.set(b: Boolean) {
		autoClassCaseEnabled = b
	}
}

class AutoInsertSemicolonState : ProjectStateToggleAction<State>(State::class.java) {
	override fun State.get() = autoInsertSemicolonEnabled
	override fun State.set(b: Boolean) {
		autoInsertSemicolonEnabled = b
	}
}

class JITRefactoringEnabled : ProjectStateToggleAction<State>(State::class.java) {
	override fun State.get() = jitRefactoringEnabled
	override fun State.set(b: Boolean) {
		jitRefactoringEnabled = b
	}
}
