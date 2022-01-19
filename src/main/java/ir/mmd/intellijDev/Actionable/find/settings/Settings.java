package ir.mmd.intellijDev.Actionable.find.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Settings implements Configurable {
	private UI ui;
	
	@Override
	public String getDisplayName() { return "Find"; }
	
	@Override
	public @Nullable JComponent createComponent() {
		ui = new UI();
		return ui.getComponent();
	}
	
	@Override
	public boolean isModified() {
		final SettingsState settingsState = SettingsState.getInstance();
		return settingsState.isCaseSensitive != ui.isCaseSensitive();
	}
	
	@Override
	public void apply() {
		final SettingsState settingsState = SettingsState.getInstance();
		settingsState.isCaseSensitive = ui.isCaseSensitive();
	}
	
	@Override
	public void reset() {
		final SettingsState settingsState = SettingsState.getInstance();
		ui.setCaseSensitive(settingsState.isCaseSensitive);
	}
	
	@Override
	public void disposeUIResources() { ui = null; }
}
