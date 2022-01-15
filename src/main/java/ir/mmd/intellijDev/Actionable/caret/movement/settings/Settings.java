package ir.mmd.intellijDev.Actionable.caret.movement.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Settings implements Configurable {
	private UI ui;
	
	@Override
	public String getDisplayName() { return "Caret Movement"; }
	
	@Override
	public @Nullable JComponent createComponent() {
		ui = new UI();
		return ui.getUi();
	}
	
	@Override
	public boolean isModified() {
		final var settingsState = SettingsState.getInstance();
		
		var modified = !ui.getWordSeparators().equals(settingsState.wordSeparators);
		modified |= ui.getNewLineIncluded() != settingsState.newLineIncluded;
		modified |= ui.getSelectedWordSeparatorsBehaviour() != settingsState.wordSeparatorsBehaviour;
		return modified;
	}
	
	@Override
	public void apply() throws ConfigurationException {
		validateWordSeparators();
		final var settingsState = SettingsState.getInstance();
		
		settingsState.wordSeparators = ui.getWordSeparators();
		settingsState.newLineIncluded = ui.getNewLineIncluded();
		settingsState.wordSeparatorsBehaviour = ui.getSelectedWordSeparatorsBehaviour();
	}
	
	@Override
	public void reset() {
		final var settingsState = SettingsState.getInstance();
		
		ui.setWordSeparators(settingsState.wordSeparators);
		ui.setNewLineIncluded(settingsState.newLineIncluded);
		ui.setSelectedWordSeparatorsBehaviour(settingsState.wordSeparatorsBehaviour);
	}
	
	@Override
	public void disposeUIResources() { ui = null; }
	
	private void validateWordSeparators() throws ConfigurationException {
		final var ws = ui.getWordSeparators();
		
		if (ws.isEmpty())
			throw new ConfigurationException("Word Separators must not be Empty");
		
		if (ws.length() != ws.chars().distinct().count())
			throw new ConfigurationException("Word Separators must contain distinct characters");
	}
}
