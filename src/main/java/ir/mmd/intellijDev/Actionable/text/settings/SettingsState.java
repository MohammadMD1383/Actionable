package ir.mmd.intellijDev.Actionable.text.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Settings State for {@code Actionable > Text}
 */
@State(
	name = "ir.mmd.intellijDev.Actionable.text.settings.SettingsState",
	storages = @Storage("Actionable.TextSettings.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	public static class Defaults {
		public static final boolean preserveCase = false;
	}
	
	public boolean preserveCase = Defaults.preserveCase;
	
	@Override
	public @Nullable SettingsState getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull SettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
