package ir.mmd.intellijDev.Actionable.typing.xml.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@com.intellij.openapi.components.State(
	name = "ir.mmd.intellijDev.Actionable.typing.xml.state.State",
	storages = @Storage("Actionable.Typing.Xml.State.xml")
)
public class State implements PersistentStateComponent<State> {
	
	public boolean collapseEmptyTagOnBackspaceEnabled = true;
	
	@Override
	public @Nullable State getState() {
		return this;
	}
	
	@Override
	public void loadState(@NotNull State state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
