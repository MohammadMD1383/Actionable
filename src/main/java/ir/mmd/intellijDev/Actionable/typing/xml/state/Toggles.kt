package ir.mmd.intellijDev.Actionable.typing.xml.state

import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.typing.StateToggleAction

@Keep
class CollapseEmptyTagOnBackspaceState : StateToggleAction<State>(State::class.java) {
	override fun State.get() = collapseEmptyTagOnBackspaceEnabled
	override fun State.set(b: Boolean) {
		collapseEmptyTagOnBackspaceEnabled = b
	}
}
