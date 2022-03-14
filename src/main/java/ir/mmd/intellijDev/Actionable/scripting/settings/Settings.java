package ir.mmd.intellijDev.Actionable.scripting.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Settings implements Configurable {
	private UI ui;
	
	@Override
	public String getDisplayName() { return "Scripting"; }
	
	@Override
	public @Nullable JComponent createComponent() {
		ui = new UI();
		return ui.getComponent();
	}
	
	@Override
	public boolean isModified() {
		final SettingsState settingsState = SettingsState.getInstance();
		
		boolean modified = !ui.getActionCode1().equals(settingsState.customAction1);
		modified |= !ui.getActionCode2().equals(settingsState.customAction2);
		modified |= !ui.getActionCode3().equals(settingsState.customAction3);
		modified |= !ui.getActionCode4().equals(settingsState.customAction4);
		modified |= !ui.getActionCode5().equals(settingsState.customAction5);
		modified |= !ui.getActionCode6().equals(settingsState.customAction6);
		modified |= !ui.getActionCode7().equals(settingsState.customAction7);
		modified |= !ui.getActionCode8().equals(settingsState.customAction8);
		modified |= !ui.getActionCode9().equals(settingsState.customAction9);
		return modified;
	}
	
	@Override
	public void apply() throws ConfigurationException {
		final SettingsState settingsState = SettingsState.getInstance();
		
		settingsState.customAction1 = ui.getActionCode1();
		settingsState.customAction2 = ui.getActionCode2();
		settingsState.customAction3 = ui.getActionCode3();
		settingsState.customAction4 = ui.getActionCode4();
		settingsState.customAction5 = ui.getActionCode5();
		settingsState.customAction6 = ui.getActionCode6();
		settingsState.customAction7 = ui.getActionCode7();
		settingsState.customAction8 = ui.getActionCode8();
		settingsState.customAction9 = ui.getActionCode9();
	}
	
	@Override
	public void reset() {
		final SettingsState settingsState = SettingsState.getInstance();
		
		ui.setActionCode1(settingsState.customAction1);
		ui.setActionCode2(settingsState.customAction2);
		ui.setActionCode3(settingsState.customAction3);
		ui.setActionCode4(settingsState.customAction4);
		ui.setActionCode5(settingsState.customAction5);
		ui.setActionCode6(settingsState.customAction6);
		ui.setActionCode7(settingsState.customAction7);
		ui.setActionCode8(settingsState.customAction8);
		ui.setActionCode9(settingsState.customAction9);
	}
	
	@Override
	public void disposeUIResources() { ui = null; }
}
