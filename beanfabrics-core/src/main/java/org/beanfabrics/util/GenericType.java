/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link GenericType} is a facade for a {@link Type} object representing a
 * Java class. It provides convenient functions for generic type reflection.
 * 
 * @author Michael Karneim
 */
public class GenericType {
	private final VariableMapping mapping;
	private final Type type;

	/**
	 * Creates a {@link GenericType} with the given mapping for the given
	 * parameterized type.
	 * 
	 * @param aMapping
	 * @param aParameterizedType
	 */
	private GenericType(VariableMapping aMapping, ParameterizedType aParameterizedType) {
		this.mapping = aMapping;
		this.type = aParameterizedType;
	}

	/**
	 * Creates a {@link GenericType} with the given mapping for the given class.
	 * 
	 * @param aMapping
	 * @param aClass
	 */
	private GenericType(VariableMapping aMapping, Class aClass) {
		this.mapping = aMapping;
		this.type = aClass;
	}

	/**
	 * Creates a {@link GenericType} with the given mapping for the given type
	 * variable.
	 * 
	 * @param aMapping
	 * @param aTypeVariable
	 */
	private GenericType(VariableMapping aMapping, TypeVariable<?> aTypeVariable) {
		this.mapping = aMapping;
		this.type = aMapping.tryResolve((TypeVariable<?>) aTypeVariable);
	}

	/**
	 * Creates a {@link GenericType} for the given {@link Class}.
	 * 
	 * @param cls
	 */
	public GenericType(Class<?> cls) {
		this(new VariableMapping(null), cls);
	}

	/**
	 * Returns the type represented by this {@link GenericType}. This can be one
	 * of {@link Class}, {@link ParameterizedType}, and {@link TypeVariable}.
	 * 
	 * @return the type represented by this {@link GenericType}.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns <code>true</code> if this type represents a {@link TypeVariable}.
	 * 
	 * @return <code>true</code> if this type represents a {@link TypeVariable}
	 */
	public boolean isTypeVariable() {
		return type instanceof TypeVariable;
	}

	/**
	 * Returns <code>true</code> if this type represents a {@link Class}.
	 * 
	 * @return <code>true</code> if this type represents a {@link Class}
	 */
	public boolean isClass() {
		return type instanceof Class;
	}

	/**
	 * Returns the class that this type represents.
	 * 
	 * @return the class this type represents
	 * @throws IllegalStateException
	 *             if this type does not represent a class
	 */
	public Class<?> asClass() throws IllegalStateException {
		if (isClass()) {
			return (Class<?>) type;
		}
		if (isParameterizedType()) {
			return (Class<?>) ((ParameterizedType) type).getRawType();
		}
		throw new IllegalStateException(String.format("Can't cast type %s to Class!", type));
	}

	/**
	 * Returns <code>true</code> if this type represents a
	 * {@link ParameterizedType}.
	 * 
	 * @return <code>true</code> if this type represents a
	 *         {@link ParameterizedType}
	 */
	public boolean isParameterizedType() {
		return type instanceof ParameterizedType;
	}

	/**
	 * Returns <code>true</code> if this type represents a {@link WildcardType}.
	 * 
	 * @return <code>true</code> if this type represents a {@link WildcardType}
	 */
	public boolean isWildcardType() {
		return type instanceof WildcardType;
	}

	/**
	 * Returns the value of the given {@link TypeVariable} as a
	 * {@link GenericType}.
	 * 
	 * @param variable
	 *            must parameterize this type or one of it's super types
	 * @return the actual value of the given {@link TypeVariable} as a
	 *         {@link GenericType}
	 * @throws IllegalArgumentException
	 *             if the variable does not parameterize this type
	 * @throws UnsupportedOperationException
	 *             if the type parameter resolution is not supported for this
	 *             type
	 */
	public GenericType getTypeParameter(TypeVariable<? extends Class<?>> variable) throws IllegalArgumentException,
			IllegalStateException {
		assertThatTypeVariableDoesParameterizeAClass(variable);
		GenericType result = null;
		if (type instanceof Class<?>) {
			Class<?> cls = (Class<?>) type;
			// Class<?> parameterizedClass = (Class<?>)
			// variable.getGenericDeclaration();
			// // Break-Condition
			// if (parameterizedClass.equals(cls)) {
			// GenericType result = new GenericType(mapping, variable);
			// return result;
			// }
			VariableMapping aMapping = resolveTypeVariableInClass(variable, cls, mapping);
			if (aMapping != null) {
				result = getGenericType(aMapping, variable);
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Class<?> rawType = (Class<?>) pType.getRawType();
			VariableMapping aMapping = resolveTypeVariableInClass(variable, rawType, mapping);
			if (aMapping != null) {
				result = getGenericType(aMapping, variable);
			}
		} else {
			throw new UnsupportedOperationException(String.format("Type parameter resolution not supported for %s",
					type));
		}
		if (result == null) {
			throw new IllegalArgumentException(String.format("%s does not parameterize this type %s!", variable, type));
		} else {
			return result;
		}
	}

	/**
	 * Returns the type of the field with the given name as a
	 * {@link GenericType}.
	 * 
	 * @param fieldname
	 *            must denote a field in this type or one of it's super types.
	 * @return the type of the field with the given name as a
	 *         {@link GenericType}
	 * @throws UnsupportedOperationException
	 *             if field type resolution is not supported for this type
	 */
	public GenericType getFieldType(String fieldname) {
		if (type instanceof ParameterizedType) {
			Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
			GenericType result = getFieldType(mapping, rawType, fieldname);
			return result;
		} else if (type instanceof Class<?>) {
			Class<?> rawType = (Class<?>) type;
			GenericType result = getFieldType(mapping, rawType, fieldname);
			return result;
		} else {
			throw new UnsupportedOperationException(String.format("field type resolution not supported for %s", type));
		}
	}

	/**
	 * Returns the return type of the method with the given name (having no
	 * parameters) as a {@link GenericType}.
	 * <p>
	 * <b>Please note:</b><br>
	 * Only methods <b>without parameters</b> are supported right now!
	 * </p>
	 * 
	 * @param methodName
	 * @return the return type of the method as a {@link GenericType}.
	 * @throws UnsupportedOperationException
	 *             if method return type resolution is not supported for this
	 *             type
	 */
	public GenericType getMethodReturnType(String methodName) {
		if (type instanceof ParameterizedType) {
			Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
			GenericType result = getMethodReturnType(mapping, rawType, methodName);
			return result;
		} else if (type instanceof Class<?>) {
			Class<?> rawType = (Class<?>) type;
			GenericType result = getMethodReturnType(mapping, rawType, methodName);
			return result;
		} else {
			throw new UnsupportedOperationException(String.format("method return type resolution not supported for %s",
					type));
		}
	}

	public Type tryResolve(Type type) {
		if (type instanceof TypeVariable<?>) {
			return tryResolve((TypeVariable<?>) type);
		} else {
			return type;
		}
	}

	public Type tryResolve(TypeVariable<?> var) {
		Type resolvedType = mapping.tryResolve(var);
		return resolvedType;
	}

	public Type narrow(Type aType, Class<?> targetType) {
		if (aType == null) {
			return null;
		}
		aType = tryResolve(aType);
		if (aType instanceof Class<?>) {
			return (Class<?>) aType;
		} else if (aType instanceof ParameterizedType) {
			return ((ParameterizedType) aType).getRawType();
		} else if (aType instanceof TypeVariable<?> || aType instanceof WildcardType) {
			Type[] bounds;
			if (aType instanceof WildcardType) {
				bounds = ((WildcardType) aType).getUpperBounds();
			} else {
				bounds = ((TypeVariable<?>) aType).getBounds();
			}
			Class<?> result = null;
			for (Type bound : bounds) {
				if (bound instanceof TypeVariable<?>) {
					Type resolvedBound = tryResolve((TypeVariable<?>) bound);
					if (resolvedBound != null) {
						bound = resolvedBound;
					}
				}
				bound = narrow(bound, targetType);
				if (bound instanceof Class<?>) {
					Class<?> cls = (Class<?>) bound;
					if (targetType != null) {
						if (targetType.isAssignableFrom(cls)) {
							if (result == null || (result != null && result.isAssignableFrom(cls))) {
								result = cls;
							}
						}
					} else {
						if (result == null || result.isAssignableFrom(cls)) {
							result = cls;
						}
					}
				}
			}
			if (result != null) {
				return result;
			} else {
				return aType;
			}
		} else {
			throw new IllegalStateException("Not Supported: " + aType.getClass());
		}
	}

	/**
	 * Resolves the variable mapping for the given type variable in the given
	 * class (or its superclass hierarchy).
	 * 
	 * @param var
	 * @param inClass
	 * @param currentMapping
	 * @return
	 */
	private static VariableMapping resolveTypeVariableInClass(TypeVariable<?> var, Class<?> inClass,
			final VariableMapping currentMapping) {
		assertThatTypeVariableDoesParameterizeAClass(var);
		Class<?> parameterizedClass = (Class<?>) var.getGenericDeclaration();
		// Break-Condition 1
		if (parameterizedClass.equals(inClass)) {
			return currentMapping;
		}

		// if the parameterized class is a super type of forClass
		if (parameterizedClass.isAssignableFrom(inClass)) {
			// process Superclass
			{
				Type inSuperType = inClass.getGenericSuperclass();
				if (inSuperType != null && isAssignableFrom(parameterizedClass, inSuperType)) {
					VariableMapping result = resolveTypeVariableInType(var, inSuperType, currentMapping);
					if (result != null) {
						return result;
					}
				}
			}

			// process Superinterfaces
			Type[] superInterfaceTypes = inClass.getGenericInterfaces();
			for (Type inSuperType : superInterfaceTypes) {
				if (isAssignableFrom(parameterizedClass, inSuperType)) {
					VariableMapping result = resolveTypeVariableInType(var, inSuperType, currentMapping);
					if (result != null) {
						return result;
					}
				}
			}
		}
		// Break-Condition 2
		// the given variable does not parameterize the given class
		return null;
	}

	private static void assertThatTypeVariableDoesParameterizeAClass(TypeVariable<?> var) {
		if (!(var.getGenericDeclaration() instanceof Class<?>)) {
			throw new IllegalArgumentException(String.format(
					"Type parameters are supported only for classes, but %s is a declared for %s", var,
					var.getGenericDeclaration()));
		}
	}

	private static VariableMapping resolveTypeVariableInParameterizedType(TypeVariable<?> var,
			ParameterizedType inParameterizedType, VariableMapping currentMapping) {
		assertThatTypeVariableDoesParameterizeAClass(var);
		currentMapping = new VariableMapping(currentMapping, inParameterizedType);
		Class<?> inClass = (Class<?>) inParameterizedType.getRawType();
		return resolveTypeVariableInClass(var, inClass, currentMapping);
	}

	private static boolean isAssignableFrom(Class<?> aClass, Type aType) {
		if (aType == null) {
			return false;
		}
		if (aType instanceof Class<?>) {
			return aClass.isAssignableFrom((Class<?>) aType);
		} else if (aType instanceof ParameterizedType) {
			return aClass.isAssignableFrom((Class<?>) ((ParameterizedType) aType).getRawType());
		} else {
			throw new UnsupportedOperationException("aType=" + aType.getClass().getName());
		}
	}

	private static VariableMapping resolveTypeVariableInType(TypeVariable<?> var, Type inType,
			final VariableMapping currentMapping) throws AssertionError {
		assertThatTypeVariableDoesParameterizeAClass(var);
		if (inType instanceof Class<?>) {
			VariableMapping result = resolveTypeVariableInClass(var, (Class<?>) inType, currentMapping);
			return result;
		} else if (inType instanceof ParameterizedType) {
			ParameterizedType inParameterizedType = (ParameterizedType) inType;
			VariableMapping result = resolveTypeVariableInParameterizedType(var, inParameterizedType, currentMapping);
			return result;
		} else {
			throw new AssertionError("Unexpected type: " + inType);
		}
	}

	private static GenericType getFieldType(VariableMapping currentMapping, Class<?> startClass, String fieldname) {
		Type type = null;
		Field field = null; // e.g. BClass<String, Integer> bObject;
		{
			Class<?> ownerClass = startClass;
			findfield: while (true) {
				Field[] fields = ownerClass.getDeclaredFields();
				for (Field f : fields) {
					if (f.getName().equals(fieldname)) {
						field = f;
						break findfield;
					}
				}
				// field not found so far
				// -> search superclass
				Type superType = ownerClass.getGenericSuperclass();
				if (superType instanceof ParameterizedType) {
					ParameterizedType parameterizedSuperType = (ParameterizedType) superType;
					currentMapping = new VariableMapping(currentMapping, parameterizedSuperType);
					ownerClass = (Class<?>) parameterizedSuperType.getRawType();
				} else if (superType instanceof Class<?>) {
					ownerClass = (Class<?>) superType;
				} else {
					throw new IllegalStateException();
				}
			}
		}

		type = field.getGenericType(); // e.g. BClass<String, Integer>

		return getGenericType(currentMapping, type);
	}

	private static GenericType getMethodReturnType(VariableMapping currentMapping, Class<?> startClass,
			String methodName) {
		Type type = null;
		Method method = null;
		{
			Class<?> ownerClass = startClass;
			findmethod: while (true) {
				Method[] methods = ownerClass.getDeclaredMethods();
				// List<Method> ma = toList(methods);
				// Collections.reverse(ma);
				// methods = ma.toArray(new Method[]{});
				for (Method m : methods) {
					if (m.getName().equals(methodName)
							&& (m.getParameterTypes() == null || m.getParameterTypes().length == 0)) {
						if (method == null || isMoreSpecific(m, method)) {
							method = m;
						}
					}
				}
				if (method != null) {
					break findmethod;
				}
				// method not found so far.
				// -> search superclass
				Type superType = ownerClass.getGenericSuperclass();
				if (superType == null) {
					// method not found
					throw new IllegalArgumentException(String.format("Method %s() not found in type %s!", methodName,
							startClass));
				} else if (superType instanceof ParameterizedType) {
					ParameterizedType parameterizedSuperType = (ParameterizedType) superType;
					currentMapping = new VariableMapping(currentMapping, parameterizedSuperType);
					ownerClass = (Class<?>) parameterizedSuperType.getRawType();
				} else if (superType instanceof Class<?>) {
					ownerClass = (Class<?>) superType;
				} else {
					throw new IllegalStateException(String.format("%s", superType));
				}
			}
		}

		type = method.getGenericReturnType();

		return getGenericType(currentMapping, type);
	}

	private static <T> List<T> toList(T... objs) {
		List<T> result = new ArrayList<T>(objs.length);
		for (T obj : objs) {
			result.add(obj);
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if method1 is more specific than method2.
	 * <p>
	 * The informal intuition is that one method is more specific than another
	 * if any invocation handled by the first method could be passed on to the
	 * other one without a compile-time type error. More precisely, if the
	 * parameter types of method1 are M1[1] to M1[n] and parameter types of
	 * method2 are M2[1] to M2[n] method1 is more specific then method2 if M1[j]
	 * can be converted to M2[j] for all j from 1 to n by method invocation
	 * conversion.
	 * 
	 * @param method1
	 * @param method2
	 * @return <code>true</code> if method m is more specific that the other
	 *         method
	 * @see <pre>
	 * <a
	 *      href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.12">The Java Language Specification (JLS) in section 15.12 Method Invocation Expression</a>
	 * </pre>
	 */
	private static boolean isMoreSpecific(Method method1, Method method2) {
		if (namesAreEqual(method1, method2) && returnTypeIsConvertible(method1, method2)
				&& parameterTypesAreConvertible(method1, method2)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean parameterTypesAreConvertible(Method method1, Method method2) {
		Class<?>[] p1 = method1.getParameterTypes();
		Class<?>[] p2 = method2.getParameterTypes();
		if (p1.length != p2.length) {
			// TODO check if we have to allow unequal parameter numbers, e.g.
			// when using ellipsis
			return false;
		}
		int len = p1.length;
		for (int i = 0; i < len; ++i) {
			if (!isConvertible(p1[i], p2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns <code>true</code> if type1 is convertible to type2.
	 * 
	 * @param type1
	 * @param type2
	 * @return <code>true</code> if type1 is convertible to type2
	 */
	private static boolean isConvertible(Class<?> type1, Class<?> type2) {
		return type2.isAssignableFrom(type1);
	}

	private static boolean returnTypeIsConvertible(Method method1, Method method2) {
		return isConvertible(method1.getReturnType(), method2.getReturnType());
	}

	private static boolean namesAreEqual(Method method1, Method method2) {
		return method1.getName().equals(method2.getName());
	}

	private static GenericType getGenericType(VariableMapping currentMapping, Type type) {
		// try to resolve type
		if (type == null) {
			throw new IllegalArgumentException("Type must not be null!");
		}

		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			currentMapping = new VariableMapping(currentMapping, parameterizedType);
			// e.g. <X1, X2 extends Number> => <String, Integer>
			return new GenericType(currentMapping, parameterizedType);
		} else if (type instanceof Class<?>) {
			return new GenericType(currentMapping, (Class) type);
		} else if (type instanceof TypeVariable<?>) {
			return new GenericType(currentMapping, (TypeVariable<?>) type);
		} else {
			throw new IllegalStateException("Type not supported so far: " + type.getClass().getName());
		}
	}

}