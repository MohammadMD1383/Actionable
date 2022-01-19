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
		final var settingsState = SettingsState.getInstance();
		
		var modified = !ui.getWordSeparators().equals(settingsState.wordSeparators);
		modified |= ui.getWordSeparatorsBehaviour() != settingsState.wordSeparatorsBehaviour;
		return modified;
	}
	
	@Override
	public void apply() throws ConfigurationException {
		validateWordSeparators();
		final var settingsState = SettingsState.getInstance();
		
		settingsState.wordSeparators = ui.getWordSeparators();
		settingsState.wordSeparatorsBehaviour = ui.getWordSeparatorsBehaviour();
	}
	
	@Override
	public void reset() {
		final var settingsState = SettingsState.getInstance();
		
		ui.setWordSeparators(settingsState.wordSeparators);
		ui.setWordSeparatorsBehaviour(settingsState.wordSeparatorsBehaviour);
	}
	
	@Override
	public void disposeUIResources() { ui = null; }
	
	/**
	 * validates the {@link SettingsState#wordSeparators} before saving it
	 *
	 * @throws ConfigurationException if required conditions for {@link SettingsState#wordSeparators} is not met
	 */
	private void validateWordSeparators() throws ConfigurationException {
		final var ws = ui.getWordSeparators();
		
		if (ws.isEmpty())
			throw new ConfigurationException("Word Separators must not be Empty");
		
		if (ws.length() != ws.chars().distinct().count())
			throw new ConfigurationException("Word Separators must contain distinct characters");
	}
}
