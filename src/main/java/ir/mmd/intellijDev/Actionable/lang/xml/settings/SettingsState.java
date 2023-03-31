package ir.mmd.intellijDev.Actionable.lang.xml.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import ir.mmd.intellijDev.Actionable.lang.xml.type.CollapseEmptyTagOnBackspace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
	name = "ir.mmd.intellijDev.Actionable.lang.xml.settings.SettingsState",
	storages = @Storage("Actionable.Lang.XML.SettingsState.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	/**
	 * @see CollapseEmptyTagOnBackspace
	 */
	public boolean collapseEmptyTagOnBackspaceEnabled = true;
	
	@Override
	public @Nullable SettingsState getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull SettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
