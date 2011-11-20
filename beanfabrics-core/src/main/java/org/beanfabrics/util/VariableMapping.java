/**
 * 
 */
package org.beanfabrics.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@link VariableMapping} maps type variables to type values.
 * 
 * @author Michael Karneim
 */
public class VariableMapping {

	private final Map<TypeVariable<?>, Type> mapping = new HashMap<TypeVariable<?>, Type>();

	/**
	 * Creates an empty variable mapping.
	 */
	public VariableMapping() {
		this(null);
	}

	/**
	 * Creates a copy of the given variable mapping.
	 * 
	 * @param aParentMapping
	 */
	public VariableMapping(VariableMapping aParentMapping) {
		if (aParentMapping != null) {
			mapping.putAll(aParentMapping.mapping);
		}
	}

	/**
	 * Creates a copy of the given variable mapping and populates it with the
	 * type variable mapping of the given parameterized type.
	 * 
	 * @param aParentMapping
	 * @param parameterizedType
	 */
	public VariableMapping(VariableMapping aParentMapping,
			ParameterizedType parameterizedType) {
		this(aParentMapping);
		this.addToMapping(parameterizedType);
	}

	/**
	 * Returns <code>true</code> if the given variable is mapped to some value.
	 * 
	 * @param variable
	 * @return <code>true</code> if the given variable is mapped to some value
	 */
	public boolean isMapped(TypeVariable<?> variable) {
		return mapping.containsKey(variable);
	}

	/**
	 * Resolves the mapped value of the given variable. Returns the mapped
	 * value.
	 * 
	 * @param variable
	 *            the variable to resolve
	 * @return the value that is mapped to this variable
	 */
	public Type resolve(TypeVariable<?> variable) {
		Type value = mapping.get(variable);
		if (value instanceof TypeVariable<?>
				&& isMapped((TypeVariable<?>) value)) {
			return resolve((TypeVariable<?>) value);
		}
		return value;
	}

	/**
	 * Tries to resolve the mapped value of the given variable. Returns the
	 * mapped value if a mapping is found, or the variable itself, if it is not
	 * found.
	 * 
	 * @param var
	 * @return
	 */
	public Type tryResolve(TypeVariable<?> var) {
		Type resolvedType = resolve(var);
		if (resolvedType != null) {
			return resolvedType;
		} else {
			return var;
		}
	}

	/**
	 * Maps the given variable to the given value.
	 * 
	 * @param variable
	 * @param toValue
	 * @throws IllegalArgumentException
	 *             if the variable is already mapped
	 */
	private void add(TypeVariable<?> variable, Type toValue)
			throws IllegalArgumentException {
		if (variable != toValue) {
			Type old = mapping.put(variable, toValue);
			if (old != null && old != toValue) {
				throw new IllegalArgumentException("var already mapped! var="
						+ variable);
			}
		}
		// throw new IllegalArgumentException();
	}

	/**
	 * Maps the given variables to the given values.
	 * 
	 * @param variables
	 * @param toValues
	 */
	private void add(TypeVariable<?>[] variables, Type[] toValues) {
		for (int i = 0; i < variables.length; i++) {
			add(variables[i], toValues[i]);
		}
	}

	/**
	 * Maps the type parameters of the given parameterized type to their actual
	 * type arguments.
	 * 
	 * @param parameterizedType
	 */
	private void addToMapping(ParameterizedType parameterizedType) {
		if (parameterizedType.getOwnerType() instanceof ParameterizedType) {
			addToMapping((ParameterizedType) parameterizedType.getOwnerType());
		}

		// .... extends MyClass<X,String>
		Type[] args = parameterizedType.getActualTypeArguments();

		// public class MyClass<X,Y> ....
		Class<?> forRawClass = (Class<?>) parameterizedType.getRawType();
		TypeVariable<?>[] params = forRawClass.getTypeParameters();

		this.add(params, args);
	}

}