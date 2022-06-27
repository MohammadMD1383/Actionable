package ir.mmd.intellijDev.Actionable.typing.xml.state

import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ProjectStateToggleAction

@Keep
class CollapseEmptyTagOnBackspaceState : ProjectStateToggleAction<State>(State::class.java) {
	override fun State.get() = collapseEmptyTagOnBackspaceEnabled
	override fun State.set(b: Boolean) {
		collapseEmptyTagOnBackspaceEnabled = b
	}
}
