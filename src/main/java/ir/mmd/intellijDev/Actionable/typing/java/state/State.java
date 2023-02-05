package ir.mmd.intellijDev.Actionable.typing.java.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * State for java typing actions
 */
@com.intellij.openapi.components.State(
	name = "ir.mmd.intellijDev.Actionable.typing.java.state.State",
	storages = @Storage("Actionable.Typing.Java.State.xml")
)
public class State implements PersistentStateComponent<State> {
	/**
	 * @see ir.mmd.intellijDev.Actionable.typing.java.AutoClassCase
	 */
	public boolean autoClassCaseEnabled = true;
	
	/**
	 * @see ir.mmd.intellijDev.Actionable.typing.java.AutoInsertSemicolon
	 */
	public boolean autoInsertSemicolonEnabled = true;
	
	/**
	 * @see ir.mmd.intellijDev.Actionable.typing.java.JITRefactoringDelete
	 * @see ir.mmd.intellijDev.Actionable.typing.java.JITRefactoringInsert
	 */
	public boolean jitRefactoringEnabled = false;
	
	@Override
	public @Nullable State getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull State state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
