package ir.mmd.intellijDev.Actionable.typing.java.state

import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.typing.StateToggleAction

@Keep
class AutoClassCaseState : StateToggleAction<State>(State::class.java) {
	override fun State.get() = autoClassCaseEnabled
	override fun State.set(b: Boolean) {
		autoClassCaseEnabled = b
	}
}

@Keep
class AutoInsertSemicolonState : StateToggleAction<State>(State::class.java) {
	override fun State.get() = autoInsertSemicolonEnabled
	override fun State.set(b: Boolean) {
		autoInsertSemicolonEnabled = b
	}
}

@Keep
class JITRefactoringEnabled : StateToggleAction<State>(State::class.java) {
	override fun State.get() = jitRefactoringEnabled
	override fun State.set(b: Boolean) {
		jitRefactoringEnabled = b
	}
}
