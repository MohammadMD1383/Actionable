package ir.mmd.intellijDev.Actionable.caret.movement.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * This class is the connector between {@link UI} and {@link SettingsState}
 */
public class Settings implements Configurable {
	/**
	 * the {@link UI} for this setting page
	 */
	private UI ui;
	
	@Override
	public String getDisplayName() { return "Caret Movement"; }
	
	@Override
	public @Nullable JComponent createComponent() {
		ui = new UI();
		return ui.getComponent();
	}
	
	@Override
	public boolean isModified() {
		final SettingsState settingsState = SettingsState.getInstance();
		
		boolean modified = !ui.getWordSeparators().equals(settingsState.wordSeparators);
		modified |= ui.getWordSeparatorsBehaviour() != settingsState.wordSeparatorsBehaviour;
		modified |= !ui.getHardStopCharacters().equals(settingsState.hardStopCharacters);
		return modified;
	}
	
	@Override
	public void apply() throws ConfigurationException {
		validateWordSeparators();
		validateHardStopCharacter();
		final SettingsState settingsState = SettingsState.getInstance();
		
		settingsState.wordSeparators = ui.getWordSeparators();
		settingsState.wordSeparatorsBehaviour = ui.getWordSeparatorsBehaviour();
		settingsState.hardStopCharacters = ui.getHardStopCharacters();
	}
	
	@Override
	public void reset() {
		final SettingsState settingsState = SettingsState.getInstance();
		
		ui.setWordSeparators(settingsState.wordSeparators);
		ui.setWordSeparatorsBehaviour(settingsState.wordSeparatorsBehaviour);
		ui.setHardStopCharacters(settingsState.hardStopCharacters);
	}
	
	@Override
	public void disposeUIResources() { ui = null; }
	
	/**
	 * validates the {@link SettingsState#wordSeparators} before saving it
	 *
	 * @throws ConfigurationException if required conditions for {@link SettingsState#wordSeparators} is not met
	 */
	private void validateWordSeparators() throws ConfigurationException {
		final String ws = ui.getWordSeparators();
		
		if (ws.isEmpty())
			throw new ConfigurationException("Word Separators must not be Empty");
		
		if (ws.length() != ws.chars().distinct().count())
			throw new ConfigurationException("Word Separators must contain distinct characters");
	}
	
	/**
	 * validates the {@link SettingsState#hardStopCharacters} before saving it
	 *
	 * @throws ConfigurationException if required conditions for {@link SettingsState#hardStopCharacters} is not met
	 */
	private void validateHardStopCharacter() throws ConfigurationException {
		final String hs = ui.getHardStopCharacters();
		
		if (hs.length() != hs.chars().distinct().count())
			throw new ConfigurationException("Hard stop characters must contain distinct characters");
	}
}
