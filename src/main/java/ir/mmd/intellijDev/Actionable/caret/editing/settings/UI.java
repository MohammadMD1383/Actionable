package ir.mmd.intellijDev.Actionable.caret.editing.settings;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ir.mmd.intellijDev.Actionable.caret.editing.settings.SettingsState.Defaults.showPasteActionHints;

public class UI {
	private JPanel component;
	private JCheckBox showPasteActionHintsCheckbox;
	private JButton showPasteActionHintsCheckboxDefaultButton;
	
	public UI() { initListeners(); }
	
	private void initListeners() {
		showPasteActionHintsCheckboxDefaultButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showPasteActionHintsCheckbox.setSelected(showPasteActionHints);
			}
		});
	}
	
	public JPanel getComponent() { return component; }
	
	public boolean isPasteActionHintsShown() { return showPasteActionHintsCheckbox.isSelected(); }
	public void setPasteActionHintsShown(boolean b) { showPasteActionHintsCheckbox.setSelected(b); }
}
