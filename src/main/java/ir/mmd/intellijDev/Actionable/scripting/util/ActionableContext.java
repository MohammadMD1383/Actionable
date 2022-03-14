package ir.mmd.intellijDev.Actionable.scripting.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import ir.mimlang.jmim.lang.ctx.Context;
import ir.mimlang.jmim.lang.ctx.Variable;
import ir.mimlang.jmim.lang.std.StdContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActionableContext extends StdContext {
	public ActionableContext(@NotNull AnActionEvent e) {
		super("Actionable");
		
		final List<Variable> variables = getVariables();
		variables.add(new ReflectorVariable("event", e));
		variables.add(getReflector());
		variables.add(getIntVal());
	}
	
	/**
	 * this function will return a {@link Class} of the fully qualified name
	 *
	 * @return a {@link Class} object
	 */
	private @NotNull Variable getReflector() {
		return new Variable() {
			@NotNull
			@Override
			public String getName() { return "Reflector"; }
			
			@NotNull
			@Override
			public Object invoke(@NotNull Context context) throws ClassNotFoundException {
				final List<Object> params = context.getParams();
				
				if (params.size() != 1)
					throw new IllegalArgumentException("Reflector only accepts 1 argument");
				
				return new ReflectorVariable("", Class.forName(params.get(0).toString()));
			}
		};
	}
	
	
	/**
	 * casts {@link Long} to {@link Integer}
	 *
	 * @return an {@link Integer} value of the given {@link Long}
	 */
	private @NotNull Variable getIntVal() {
		return new Variable() {
			@Override
			public @NotNull String getName() { return "intVal"; }
			
			@Override
			public @NotNull Integer invoke(@NotNull Context context) {
				return Math.toIntExact((Long) context.getParams().get(0));
			}
		};
	}
}
