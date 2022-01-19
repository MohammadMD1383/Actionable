package ir.mmd.intellijDev.Actionable.find.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsState implements PersistentStateComponent<SettingsState> {
	public static class Defaults {
		public static boolean IS_CASE_SENSITIVE = true;
	}
	
	public boolean isCaseSensitive = Defaults.IS_CASE_SENSITIVE;
	
	public static SettingsState getInstance() { return ApplicationManager.getApplication().getService(SettingsState.class); }
	
	@Override
	public @Nullable SettingsState getState() { return this; }
	
	@Override
	public void loadState(@NotNull SettingsState state) { XmlSerializerUtil.copyBean(state, this); }
}
