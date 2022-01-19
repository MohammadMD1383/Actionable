package ir.mmd.intellijDev.Actionable.caret.movement.settings;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI {
	private JPanel component;
	private JTextField wordSeparatorsField;
	private JCheckBox newLineIncluded;
	private JCheckBox tabIncluded;
	private ButtonGroup wordSeparatorsBehaviourGroup;
	private JRadioButton stopAtCharacterTypeChangeRadioButton;
	private JRadioButton stopAtNextSameCharacterTypeRadioButton;
	private JButton wordSeparatorsFieldDefault;
	private JButton wordSeparatorsBehaviourDefault;
	
	public UI() {
		initListeners();
	}
	
	private void initListeners() {
		wordSeparatorsFieldDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UI.this.setWordSeparators(SettingsState.Defaults.wordSeparators);
			}
		});
		
		wordSeparatorsBehaviourDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UI.this.setWordSeparatorsBehaviour(SettingsState.Defaults.wordSeparatorsBehaviour);
			}
		});
	}
	
	public JPanel getComponent() { return component; }
	
	public String getWordSeparators() {
		var ws = wordSeparatorsField.getText();
		if (newLineIncluded.isSelected()) ws += '\n';
		if (tabIncluded.isSelected()) ws += '\n';
		return ws;
	}
	public void setWordSeparators(@NotNull String s) {
		var newLine = s.contains("\n");
		var tab = s.contains("\t");
		
		wordSeparatorsField.setText(s.replaceAll("[\n\t]", ""));
		newLineIncluded.setSelected(newLine);
		tabIncluded.setSelected(tab);
	}
	
	public int getWordSeparatorsBehaviour() { return Integer.parseInt(wordSeparatorsBehaviourGroup.getSelection().getActionCommand()); }
	public void setWordSeparatorsBehaviour(int i) {
		wordSeparatorsBehaviourGroup.setSelected(
			i == SettingsState.WSBehaviour.STOP_AT_CHAR_TYPE_CHANGE ?
				stopAtCharacterTypeChangeRadioButton.getModel() :
				stopAtNextSameCharacterTypeRadioButton.getModel(),
			true
		);
	}
}
