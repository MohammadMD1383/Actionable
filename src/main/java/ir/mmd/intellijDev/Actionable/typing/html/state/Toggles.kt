package ir.mmd.intellijDev.Actionable.typing.html.state

import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

class ExpandTagOnTypeState : ProjectStateToggleAction<State>(State::class.java) {
	override fun State.get() = expandTagOnTypeEnabled
	override fun State.set(b: Boolean) {
		expandTagOnTypeEnabled = b
	}
}
