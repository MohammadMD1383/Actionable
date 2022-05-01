package ir.mmd.intellijDev.Actionable.util

import ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState as EditingSettingsState
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState as MovementSettingsState
import ir.mmd.intellijDev.Actionable.find.settings.SettingsState as FindSettingsState

inline fun <T> by(receiver: T, block: (T) -> Unit) = block(receiver)
inline fun <T, R> returnBy(receiver: T, block: (T) -> R) = block(receiver)

inline fun after(block: () -> Unit) = block()

inline fun <T> nonnull(receiver: T?, block: (T) -> Unit) {
	if (receiver != null) block(receiver)
}

inline fun countRepeats(initial: Int = 1, addition: Int = 1, block: (Int) -> Boolean): Int {
	var count = initial
	while (block(count)) count += addition
	return count
}

inline fun <R> withFindSettings(block: FindSettingsState.() -> R) = FindSettingsState.getInstance().run(block)
inline fun <R> withEditingSettings(block: EditingSettingsState.() -> R) = EditingSettingsState.getInstance().run(block)
inline fun <R> withMovementSettings(block: MovementSettingsState.() -> R) = MovementSettingsState.getInstance().run(block)
