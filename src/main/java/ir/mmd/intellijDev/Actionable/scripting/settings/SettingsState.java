package ir.mmd.intellijDev.Actionable.scripting.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
	name = "ir.mmd.intellijDev.Actionable.scripting.settings.SettingsState",
	storages = @Storage("Actionable.ScriptingSettings.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	public String customAction1 = "";
	public String customAction2 = "";
	public String customAction3 = "";
	public String customAction4 = "";
	public String customAction5 = "";
	public String customAction6 = "";
	public String customAction7 = "";
	public String customAction8 = "";
	public String customAction9 = "";
	
	@SuppressWarnings("deprecation")
	public static SettingsState getInstance() { return ServiceManager.getService(SettingsState.class); }
	
	@Override
	public @Nullable SettingsState getState() { return this; }
	
	@Override
	public void loadState(@NotNull SettingsState state) { XmlSerializerUtil.copyBean(state, this); }
}
