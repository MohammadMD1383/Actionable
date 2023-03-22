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
	public static class WSBehaviour {
		/**
		 * example of moving caret with this mode
		 * <pre>
		 *     hello| world from java
		 *     hello |world from java
		 *     hello world| from java
		 *     hello world |from java
		 *     ...
		 * </pre>
		 */
		public static final int STOP_AT_CHAR_TYPE_CHANGE = 0;
		
		/**
		 * example of moving caret with this mode
		 * <pre>
		 *     hello| world from java
		 *     hello world| from java
		 *     hello world from| java
		 *     hello world from java|
		 *     ...
		 * </pre>
		 */
		public static final int STOP_AT_NEXT_SAME_CHAR_TYPE = 1;
	}
	
	public enum SEMBehaviour {
		Start, Offset, End
	}
	
	/**
	 * this class holds default values for settings
	 */
	public static class Defaults {
		public static final @NotNull String wordSeparators = "`~!@#$%^&*()-=+[{]}\\|;:'\",.<>/?\n\t";
		public static final int wordSeparatorsBehaviour = WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE;
		public static final @NotNull String hardStopSeparators = " ";
		public static @NotNull SettingsState.SEMBehaviour sameElementMovementBehaviour = SEMBehaviour.Start;
	}
	
	public @NotNull String wordSeparators = Defaults.wordSeparators;
	
	public int wordSeparatorsBehaviour = Defaults.wordSeparatorsBehaviour;
	
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
