package ir.mmd.intellijDev.Actionable.caret.movement.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
	name = "ir.mmd.intellijDev.Actionable.caret.movement.settings.CaretMovementSettingsState",
	storages = @Storage("Actionable.CaretMovementSettingsState.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	public static class WSBehaviour {
		public static final int STOP_AT_CHAR_TYPE_CHANGE = 0;
		public static final int STOP_AT_NEXT_SAME_CHAR_TYPE = 1;
	}
	
	public static class Defaults {
		public static final @NotNull String wordSeparators = "`~!@#$%^&*()-=+[{]}\\|;:'\",.<>/? ";
		public static final boolean newLineInclude = true;
		public static final int wordSeparatorsBehaviour = WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE;
	}
	
	public @NotNull String wordSeparators = Defaults.wordSeparators;
	public boolean newLineIncluded = Defaults.newLineInclude;
	public int wordSeparatorsBehaviour = Defaults.wordSeparatorsBehaviour;
	
	public static SettingsState getInstance() { return ApplicationManager.getApplication().getService(SettingsState.class); }
	
	@Override
	public @Nullable SettingsState getState() { return this; }
	
	@Override
	public void loadState(@NotNull SettingsState state) { XmlSerializerUtil.copyBean(state, this); }
}
