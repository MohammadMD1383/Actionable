package ir.mmd.intellijDev.Actionable.typing.java.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import ir.mmd.intellijDev.Actionable.internal.proguard.Keep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Keep
@com.intellij.openapi.components.State(
	name = "ir.mmd.intellijDev.Actionable.typing.java.state.State",
	storages = @Storage("Actionable.Typing.Java.State.xml")
)
public class State implements PersistentStateComponent<State> {
	@Keep
	public boolean autoClassCaseEnabled = true;
	
	@Keep
	public boolean autoInsertSemicolonEnabled = true;
	
	@Keep
	public boolean jitRefactoringEnabled = false;
	
	@SuppressWarnings("deprecation")
	public static @NotNull State getInstance() {
		return ServiceManager.getService(State.class);
	}
	
	@Override
	public @Nullable State getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull State state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
