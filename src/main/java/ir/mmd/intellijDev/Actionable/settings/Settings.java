package ir.mmd.intellijDev.Actionable.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Settings implements Configurable {
	@Override
	public String getDisplayName() {
		return "Actionable";
	}
	
	@Override
	public @Nullable JComponent createComponent() {
		return null;
	}
	
	@Override
	public boolean isModified() { return false; }
	
	@Override
	public void apply() { }
}
