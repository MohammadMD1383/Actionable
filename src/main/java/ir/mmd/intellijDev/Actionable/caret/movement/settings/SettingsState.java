package ir.mmd.intellijDev.Actionable.caret.movement.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Settings State for {@code Actionable > Caret > Editing}
 */
@State(
	name = "ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState",
	storages = @Storage("Actionable.CaretMovementSettingsState.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	/**
	 * <b>enum</b><br>
	 * for behaviour of <code>move caret ...</code> actions<br>
	 * see {@link UI} for what these constants mean
	 */
	public enum WSBehaviour {
		/**
		 * Example of moving caret with this mode
		 * <pre>
		 *     hello| world from java
		 *     hello |world from java
		 *     hello world| from java
		 *     hello world |from java
		 *     ...
		 * </pre>
		 */
		StopAtCharTypeChange,
		
		/**
		 * Example of moving caret with this mode
		 * <pre>
		 *     hello| world from java
		 *     hello world| from java
		 *     hello world from| java
		 *     hello world from java|
		 *     ...
		 * </pre>
		 */
		StopAtNextSameCharType
	}
	
	public enum SEMBehaviour {
		Start, Offset, End
	}
	
	/**
	 * this class holds default values for settings
	 */
	public static class Defaults {
		public static final @NotNull String wordSeparators = "`~!@#$%^&*()-=+[{]}\\|;:'\",.<>/?\n\t";
		public static final @NotNull WSBehaviour wordSeparatorsBehaviour = WSBehaviour.StopAtCharTypeChange;
		public static final @NotNull String hardStopSeparators = " ";
		public static @NotNull SettingsState.SEMBehaviour sameElementMovementBehaviour = SEMBehaviour.Start;
	}
	
	public @NotNull String wordSeparators = Defaults.wordSeparators;
	
	public @NotNull WSBehaviour wordSeparatorsBehaviour = Defaults.wordSeparatorsBehaviour;
	
	public @NotNull String hardStopCharacters = Defaults.hardStopSeparators;
	
	public @NotNull SettingsState.SEMBehaviour sameElementMovementBehaviour = Defaults.sameElementMovementBehaviour;
	
	@Override
	public @Nullable SettingsState getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull SettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
