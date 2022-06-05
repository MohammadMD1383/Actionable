package ir.mmd.intellijDev.Actionable.typing.xml.state

import ir.mmd.intellijDev.Actionable.typing.StateToggleAction

class CollapseEmptyTagOnBackspaceState : StateToggleAction<State>(State::class.java) {
	override fun State.get() = collapseEmptyTagOnBackspaceEnabled
	override fun State.set(b: Boolean) {
		collapseEmptyTagOnBackspaceEnabled = b
	}
}
