package ir.mmd.intellijDev.Actionable.find.settings;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI {
	private JPanel component;
	private JCheckBox caseSensitiveCheckBox;
	private JButton caseSensitiveCheckBoxDefault;
	
	public UI() {
		initListeners();
	}
	
	private void initListeners() {
		caseSensitiveCheckBoxDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				caseSensitiveCheckBox.setSelected(SettingsState.Defaults.IS_CASE_SENSITIVE);
			}
		});
	}
	
	public JPanel getComponent() {
		return component;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitiveCheckBox.isSelected();
	}
	
	public void setCaseSensitive(boolean b) {
		caseSensitiveCheckBox.setSelected(b);
	}
}
