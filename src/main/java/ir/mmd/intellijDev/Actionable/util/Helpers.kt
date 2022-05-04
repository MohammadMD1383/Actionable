package ir.mmd.intellijDev.Actionable.util

import ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState as EditingSettingsState
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState as FindSettingsState

inline fun <T> by(receiver: T, block: (T) -> Unit) = block(receiver)
inline fun <T, R> returnBy(obj: T, block: (T) -> R) = block(obj)
inline fun <T1, T2, R> returnBy(obj1: T1, obj2: T2, block: (T1, T2) -> R) = block(obj1, obj2)

inline fun <T, O, R> with(receiver: T, obj: O, block: T.(O) -> R) = receiver.block(obj)

inline fun after(block: () -> Unit) = block()
inline fun trueAfter(block: () -> Unit): Boolean {
	block()
	return true
}

inline fun <T> nonnull(receiver: T?, block: (T) -> Unit) {
	if (receiver != null) block(receiver)
}

inline fun <R> withFindSettings(block: FindSettingsState.() -> R) = FindSettingsState.getInstance().run(block)
inline fun <R> withEditingSettings(block: EditingSettingsState.() -> R) = EditingSettingsState.getInstance().run(block)
inline fun <R> withMovementSettings(block: MovementSettingsState.() -> R) = MovementSettingsState.getInstance().run(block)
