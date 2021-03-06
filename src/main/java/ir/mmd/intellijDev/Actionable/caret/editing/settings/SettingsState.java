package ir.mmd.intellijDev.Actionable.caret.editing.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Keep
@State(
	name = "ir.mmd.intellijDev.Actionable.caret.editing.settings",
	storages = @Storage("Actionable.CaretEditingSettingsState.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	/**
	 * This class holds the default values for the settings
	 */
	public static class Defaults {
		public static final boolean showPasteActionHints = true;
	}
	
	@Keep
	public boolean showPasteActionHints = Defaults.showPasteActionHints;
	
	@Override
	public @Nullable SettingsState getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull SettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
