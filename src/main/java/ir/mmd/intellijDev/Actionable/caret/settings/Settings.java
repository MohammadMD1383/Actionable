package ir.mmd.intellijDev.Actionable.caret.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Settings implements Configurable {
	@Override
	public String getDisplayName() { return "Caret"; }
	
	@Override
	public @Nullable JComponent createComponent() { return null; }
	
	@Override
	public boolean isModified() { return false; }
	
	@Override
	public void apply() { }
}
