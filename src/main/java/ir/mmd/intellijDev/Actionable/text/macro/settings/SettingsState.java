package ir.mmd.intellijDev.Actionable.text.macro.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Keep
@State(
	name = "ir.mmd.intellijDev.Actionable.text.macro.settings.SettingsState",
	storages = @Storage("Actionable.Text.MacroSettingsState.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	@Keep
	public Map<String, String> macros = new HashMap<>();
	
	@Override
	public SettingsState getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull SettingsState state) {
		this.macros = state.macros;
	}
}
