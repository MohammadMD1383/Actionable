package ir.mmd.intellijDev.Actionable.text.settings;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class UI {
	private JPanel component;
	private JCheckBox preserveCaseCheckbox;
	
	public @NotNull JPanel getComponent() {
		return component;
	}
	
	public boolean getPreserveCase() {
		return preserveCaseCheckbox.isSelected();
	}
	
	public void setPreserveCase(boolean b) {
		preserveCaseCheckbox.setSelected(b);
	}
}
