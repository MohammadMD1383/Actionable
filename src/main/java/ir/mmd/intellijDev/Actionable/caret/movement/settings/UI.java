package ir.mmd.intellijDev.Actionable.caret.movement.settings;

import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState.SEMBehaviour;
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState.WSBehaviour;
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
	private JTextField hardStopCharactersField;
	private JButton hardStopCharactersDefault;
	private JCheckBox hardStopCharactersIncludeNewLine;
	private JCheckBox hardStopCharactersIncludeTab;
	private ButtonGroup sameElementMovementBehaviourGroup;
	private JRadioButton startRadioButton;
	private JRadioButton offsetBasedRadioButton;
	private JRadioButton endRadioButton;
	private JButton sameElementMovementBehaviourDefault;
	
	public UI() {
		stopAtCharacterTypeChangeRadioButton.setActionCommand(WSBehaviour.StopAtCharTypeChange.name());
		stopAtNextSameCharacterTypeRadioButton.setActionCommand(WSBehaviour.StopAtNextSameCharType.name());
		startRadioButton.setActionCommand(SEMBehaviour.Start.name());
		offsetBasedRadioButton.setActionCommand(SEMBehaviour.Offset.name());
		endRadioButton.setActionCommand(SEMBehaviour.End.name());
		
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
		
		hardStopCharactersDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UI.this.setHardStopCharacters(SettingsState.Defaults.hardStopSeparators);
			}
		});
		
		sameElementMovementBehaviourDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UI.this.setSameElementMovementBehaviour(SettingsState.Defaults.sameElementMovementBehaviour);
			}
		});
	}
	
	public JPanel getComponent() {
		return component;
	}
	
	public String getWordSeparators() {
		var ws = wordSeparatorsField.getText();
		if (newLineIncluded.isSelected()) ws += '\n';
		if (tabIncluded.isSelected()) ws += '\t';
		return ws;
	}
	
	public void setWordSeparators(@NotNull String s) {
		var newLine = s.contains("\n");
		var tab = s.contains("\t");
		
		wordSeparatorsField.setText(s.replaceAll("[\n\t]", ""));
		newLineIncluded.setSelected(newLine);
		tabIncluded.setSelected(tab);
	}
	
	public WSBehaviour getWordSeparatorsBehaviour() {
		return WSBehaviour.valueOf(wordSeparatorsBehaviourGroup.getSelection().getActionCommand());
	}
	
	public void setWordSeparatorsBehaviour(WSBehaviour behaviour) {
		final var targetModel = switch (behaviour) {
			case StopAtCharTypeChange -> stopAtCharacterTypeChangeRadioButton.getModel();
			case StopAtNextSameCharType -> stopAtNextSameCharacterTypeRadioButton.getModel();
		};
		
		wordSeparatorsBehaviourGroup.setSelected(targetModel, true);
	}
	
	public String getHardStopCharacters() {
		var hs = hardStopCharactersField.getText();
		if (hardStopCharactersIncludeNewLine.isSelected()) hs += "\n";
		if (hardStopCharactersIncludeTab.isSelected()) hs += "\t";
		return hs;
	}
	
	public void setHardStopCharacters(@NotNull String s) {
		final var newLine = s.contains("\n");
		final var tab = s.contains("\t");
		
		hardStopCharactersField.setText(s.replaceAll("[\n\t]", ""));
		hardStopCharactersIncludeNewLine.setSelected(newLine);
		hardStopCharactersIncludeTab.setSelected(tab);
	}
	
	public SEMBehaviour getSameElementMovementBehaviour() {
		return SEMBehaviour.valueOf(sameElementMovementBehaviourGroup.getSelection().getActionCommand());
	}
	
	public void setSameElementMovementBehaviour(SEMBehaviour behaviour) {
		final var targetModel = switch (behaviour) {
			case Start -> startRadioButton.getModel();
			case Offset -> offsetBasedRadioButton.getModel();
			case End -> endRadioButton.getModel();
		};
		
		sameElementMovementBehaviourGroup.setSelected(targetModel, true);
	}
}
