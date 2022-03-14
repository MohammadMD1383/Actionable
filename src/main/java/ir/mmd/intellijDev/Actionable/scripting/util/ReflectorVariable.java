package ir.mmd.intellijDev.Actionable.scripting.util;

import ir.mimlang.jmim.lang.ctx.Context;
import ir.mimlang.jmim.lang.ctx.Variable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * this class bridges over MiM {@link ir.mimlang.jmim.lang.node.MemberAccessNode} to java equivalents
 */
public class ReflectorVariable implements Variable {
	private final @NotNull String name;
	private @Nullable Object value;
	
	public ReflectorVariable(
		@NotNull String name,
		@Nullable Object value
	) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public @NotNull String getName() { return name; }
	
	@Override
	public @Nullable Object getValue() { return this; }
	
	@Override
	public void setValue(@Nullable Object value) {
		if (value instanceof ReflectorVariable)
			this.value = ((ReflectorVariable) value).value;
		else
			this.value = value;
	}
	
	@Override
	public @Nullable Object getProperty(@NotNull String name) throws IllegalAccessException {
		if (name.equals("this"))
			return value;
		
		Field field = findField(name);
		if (field != null)
			return new ReflectorVariable("", field.get(value));
		else
			throw new IllegalAccessException("No field named " + name);
	}
	
	@Override
	public void setProperty(
		@NotNull String name,
		@Nullable Object value
	) throws IllegalAccessException {
		final Field field = findField(name);
		if (field != null)
			field.set(this.value, value);
		else
			throw new IllegalAccessException("No field named " + name);
	}
	
	@Override
	public @Nullable Object invokeMember(
		@NotNull String name,
		@NotNull Context context
	) throws InvocationTargetException, IllegalAccessException {
		final Object[] args = context.getParams().toArray();
		Method foundMethod = findMethod(name);
		
		if (foundMethod != null)
			return new ReflectorVariable("", foundMethod.invoke(value, args));
		else
			throw new IllegalAccessException("No method named " + name);
	}
	
	private @Nullable Field findField(@NotNull String name) {
		final Field[] fields = (value instanceof Class) ? ((Class<?>) value).getFields() : value.getClass().getFields();
		for (Field field : fields)
			if (field.getName().equals(name))
				return field;
		
		return null;
	}
	
	private @Nullable Method findMethod(@NotNull String name) {
		final Method[] methods = (value instanceof Class) ? ((Class<?>) value).getMethods() : value.getClass().getMethods();
		for (Method method : methods)
			if (method.getName().equals(name))
				return method;
		
		return null;
	}
}
