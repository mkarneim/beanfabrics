/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.util;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The <code>GenericsUtil</code> is a utility class for accessing generic type information at runtime. 
 * <p>
 * You can use this class to find out the actual type arguments assigned to a given generic class
 * by a given child class.
 * <p>
 * Examples:
 * <ul><li><p>
 * For the following interface that is a child of the generic interface Collection
 * <p><code><br>
 * public interface StringCollection extends Collection&lt;String&gt; { }
 * </code></p><br>
 * you can get the type argument by calling
 * <p><code><br>
 * List list = util.getTypeArguments(Collection.class, StringCollection.class);<br>
 * Class typeArg = (Class)list.get(0);<br>
 * </code></p><br>
 * The value of <code>typeArg</code> will be <code>String.class</code>.
 * <p></li><p>
 * 
 * <li><p>
 * For the following <i>generic</i> interface that is a child of the generic interface Collection
 * <p><code><br>
 * public interface ThrowableCollection&lt;T extends Throwable&gt; extends Collection&lt;T&gt; { }
 * </code></p><br>
 * you can get the type argument by calling
 * <p><code><br>
 * List list = util.getTypeArguments(Collection.class, ThrowableCollection.class);<br>
 * TypeVariable typeArg = (TypeVariable)list.get(0);<br>
 * Type bound = typeArg.getBounds()[0];<br>
 * </code></p><br>
 * The value of <code>bound</code> will be <code>Throwable.class</code>.
 * <p></li>
 * </ul>
 * This class is inspired by Ian Robertson, see
 * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
 * 
 * @author Michael Karneim
 * 
 */
public class GenericsUtil {
	/**
	 * Get the actual type arguments a field has used to parameterize it's generic type.
	 * @param genericClass
	 * @param ownerClass
	 * @param fieldName
	 * @return a list of the actual type arguments.
	 */
	public static List<Type> getFieldTypeArguments(Class<?> genericClass, Class<?> ownerClass, String fieldName) {
		Collection<Field> fields = ReflectionUtil.getAllFields(ownerClass, fieldName, genericClass);
		if ( fields.isEmpty()) {
			throw new IllegalArgumentException("Class "
					+ownerClass.getName()+" nor it's superclasses do declare a field '"
					+fieldName+"' with type '"+genericClass.getName()+"'");
		} else if ( fields.size()>1) {
			throw new IllegalArgumentException("Ambiguous field name '"+fieldName+"' in class "+ownerClass.getName());
		} 
		Field field = fields.iterator().next();
		return getFieldTypeArguments(genericClass, field);
	}
	
	private static List<Type> getFieldTypeArguments(Class<?> genericClass, Field field) {
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		
		internalGetFieldTypeArguments(genericClass, field, resolvedTypes);
		
		// finally, for each type parameter of baseClass,
		// determine (if possible) the raw class
		Type[] typeParameters = genericClass.getTypeParameters();
		List<Type> typeArguments = new ArrayList<Type>();
		
		// resolve types by chasing down type variables.
		for (Type baseType : typeParameters) {
			while (resolvedTypes.containsKey(baseType)) {
				baseType = resolvedTypes.get(baseType);
			}
			typeArguments.add(baseType);
		}
		
		return typeArguments;
	}
	
	private static void internalGetFieldTypeArguments(Class<?> baseClass, Field field, Map<Type, Type> resolvedTypes) {
		Type fieldType = field.getGenericType();
		internalGetTypeArguments(baseClass, fieldType, resolvedTypes);
	}
	
	/**
	 * Get the actual type arguments a child class has used to extend a generic class.
	 * 
	 * @param genericClass
	 *            the generic class (or interface)
	 * @param childClass
	 *            the child class
	 * @return a list of the actual type arguments.
	 */
	public static <T> List<Type> getTypeArguments(Class<T> genericClass, Class<? extends T> childClass) {
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		
		internalGetTypeArguments(genericClass, childClass, resolvedTypes);
		
		// finally, for each type parameter of baseClass,
		// determine (if possible) the raw class
		Type[] typeParameters = genericClass.getTypeParameters();
		List<Type> typeArguments = new ArrayList<Type>();
		
		// resolve types by chasing down type variables.
		for (Type baseType : typeParameters) {
			while (resolvedTypes.containsKey(baseType)) {
				baseType = resolvedTypes.get(baseType);
			}
			typeArguments.add(baseType);
		}
		
		return typeArguments;
	}
	
	private static void internalGetTypeArguments(Class<?> baseClass, Type type, Map<Type, Type> resolvedTypes) {
		if ( type == null || baseClass.isAssignableFrom(getClass(type))==false) {
			// this is the break condition. Can't walk up any further in inheritance hierarchy.
			return;
		} else {
			if (type instanceof Class) {
				// process superclass
				Type nextType = ((Class) type).getGenericSuperclass();
				internalGetTypeArguments(baseClass, nextType, resolvedTypes);
				// process interfaces
				Type[] nextTypes = ((Class) type).getGenericInterfaces();
				for( Type nextIType: nextTypes) {
					internalGetTypeArguments(baseClass, nextIType, resolvedTypes);
				}
			} else {
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class<?> rawType = (Class) parameterizedType.getRawType();

				// class NAME extends GENERIC<A1, A2, ...>
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				// class GENERIC<P1, P2, ...>
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
				
				for (int i = 0; i < actualTypeArguments.length; i++) {
					// set argument for parameter
					// map.put( P1, A1);
					resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
				}

				// process superclass
				Type nextType = rawType.getGenericSuperclass();
				internalGetTypeArguments(baseClass, nextType, resolvedTypes);
				// process interfaces
				Type[] nextTypes = rawType.getGenericInterfaces();
				for( Type nextIType: nextTypes) {
					internalGetTypeArguments(baseClass, nextIType, resolvedTypes);
				}
			}
		}
	}
	
	/**
	 * Get the underlying class for a type, or null if the type is a variable
	 * type.
	 * 
	 * @param type
	 *            the type
	 * @return the underlying class
	 */
	private static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	
}