package ir.mmd.intellijDev.Actionable.lang.java.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import ir.mmd.intellijDev.Actionable.lang.java.type.AutoClassCase;
import ir.mmd.intellijDev.Actionable.lang.java.type.AutoInsertSemicolon;
import ir.mmd.intellijDev.Actionable.lang.java.type.JITRefactoringDelete;
import ir.mmd.intellijDev.Actionable.lang.java.type.JITRefactoringInsert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
	name = "ir.mmd.intellijDev.Actionable.lang.java.settings.SettingsState",
	storages = @Storage("Actionable.Lang.Java.SettingsState.xml")
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
	/**
	 * @see AutoClassCase
	 */
	public boolean autoClassCaseEnabled = true;
	
	/**
	 * @see AutoInsertSemicolon
	 */
	public boolean autoInsertSemicolonEnabled = true;
	
	/**
	 * @see JITRefactoringDelete
	 * @see JITRefactoringInsert
	 */
	public boolean jitRefactoringEnabled = false;
	
	@Override
	public @Nullable SettingsState getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull SettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
