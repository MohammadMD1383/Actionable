package ir.mmd.intellijDev.Actionable.caret.editing.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Settings implements Configurable {
	private UI ui;
	
	@Override
	public String getDisplayName() { return "Editing"; }
	
	@Override
	public @Nullable JComponent createComponent() {
		ui = new UI();
		return ui.getComponent();
	}
	
	@Override
	public boolean isModified() {
		final SettingsState settingsState = SettingsState.getInstance();
		return ui.isPasteActionHintsShown() != settingsState.showPasteActionHints;
	}
	
	@Override
	public void apply() {
		final SettingsState settingsState = SettingsState.getInstance();
		settingsState.showPasteActionHints = ui.isPasteActionHintsShown();
	}
	
	@Override
	public void reset() {
		final SettingsState settingsState = SettingsState.getInstance();
		ui.setPasteActionHintsShown(settingsState.showPasteActionHints);
	}
	
	@Override
	public void disposeUIResources() { ui = null; }
}
