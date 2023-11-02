package ir.mmd.intellijDev.Actionable.find.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Settings State for {@code Actionable > Find}
 */
@State(
	name = "ir.mmd.intellijDev.Actionable.find.settings.SettingsState",
	storages = @Storage("Actionable.FindSettingsState.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	/**
	 * This class contains the default values for the settings
	 */
	public static class Defaults {
		public static final boolean IS_CASE_SENSITIVE = true;
	}
	
	public boolean isCaseSensitive = Defaults.IS_CASE_SENSITIVE;
	
	@Override
	public @Nullable SettingsState getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull SettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
