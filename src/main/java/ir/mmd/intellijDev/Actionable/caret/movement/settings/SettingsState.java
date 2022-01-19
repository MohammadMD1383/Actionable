package ir.mmd.intellijDev.Actionable.caret.movement.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class provides the persistent state for the settings
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
		public static final int STOP_AT_CHAR_TYPE_CHANGE = 0;
		public static final int STOP_AT_NEXT_SAME_CHAR_TYPE = 1;
	}
	
	/**
	 * this class holds default values for settings
	 */
	public static class Defaults {
		public static final @NotNull String wordSeparators = "`~!@#$%^&*()-=+[{]}\\|;:'\",.<>/? \n\t";
		public static final int wordSeparatorsBehaviour = WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE;
	}
	
	/**
	 * see {@link UI} for detailed description
	 */
	public @NotNull String wordSeparators = Defaults.wordSeparators;
	
	/**
	 * see {@link UI} for detailed description
	 */
	public int wordSeparatorsBehaviour = Defaults.wordSeparatorsBehaviour;
	
	@SuppressWarnings("deprecation")
	public static SettingsState getInstance() { return ServiceManager.getService(SettingsState.class); }
	
	@Override
	public @Nullable SettingsState getState() { return this; }
	
	@Override
	public void loadState(@NotNull SettingsState state) { XmlSerializerUtil.copyBean(state, this); }
}
