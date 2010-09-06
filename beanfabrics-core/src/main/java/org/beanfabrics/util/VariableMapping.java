/**
 * 
 */
package org.beanfabrics.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class VariableMapping {
    private final VariableMapping parentMapping;
    private final Map<TypeVariable<?>, Type> mapping = new HashMap<TypeVariable<?>, Type>();

    VariableMapping(VariableMapping aParentMapping, ParameterizedType forPClass) {
        parentMapping = aParentMapping;
        addParameterMapping(forPClass);
    }

    public VariableMapping(VariableMapping aParentMapping) {
        parentMapping = aParentMapping;
    }

    public VariableMapping createMapping(ParameterizedType forPClass) {
        VariableMapping result = new VariableMapping(this, forPClass);
        return result;
    }

    private boolean containsKey(TypeVariable<?> var) {
        return mapping.containsKey(var) || (parentMapping != null && parentMapping.containsKey(var));
    }

    private boolean containsValue(Type type) {
        return mapping.containsValue(type) || (parentMapping != null && parentMapping.containsValue(type));
    }

    public boolean contains(TypeVariable<?> var) {
        return containsKey(var) || containsValue(var);
    }

    public Type resolve(TypeVariable<?> var) {
        Type value = mapping.get(var);
        if (value == null && parentMapping != null) {
            value = parentMapping.resolve(var);
        }
        if (value == null && containsKey(var)) {
            return var;
        }
        if (value instanceof TypeVariable<?> && containsKey((TypeVariable<?>)value)) {
            return resolve((TypeVariable<?>)value);
        }
        return value;
    }

    public Type tryResolve(Type type) {
        if (type instanceof TypeVariable<?>) {
            return tryResolve((TypeVariable<?>)type);
        } else {
            return type;
        }
    }

    public Type tryResolve(TypeVariable<?> var) {
        Type resolvedType = resolve(var);
        if (resolvedType != null) {
            return resolvedType;
        } else {
            return var;
        }
    }

    private void put(TypeVariable<?> var, Type value) {
        if (var != value) {
            Type old = mapping.put(var, value);
            if (old != null && old != value) {
                throw new IllegalArgumentException("var already mapped! var=" + var);
            }
        }
        // throw new IllegalArgumentException();
    }

    private void putAll(TypeVariable<?>[] vars, Type[] values) {
        for (int i = 0; i < vars.length; i++) {
            put(vars[i], values[i]);
        }
    }

    private void addParameterMapping(ParameterizedType forPClass) {
        // .... extends MyClass<X,String> 
        Type[] args = forPClass.getActualTypeArguments();

        // public class MyClass<X,Y> ....
        Class<?> forRawClass = (Class<?>)forPClass.getRawType();
        TypeVariable<?>[] params = forRawClass.getTypeParameters();

        this.putAll(params, args);
        processOuterClass(forPClass);
    }

    private void processOuterClass(ParameterizedType forPClass) {
        Type declaringType = forPClass.getOwnerType();
        if (declaringType instanceof ParameterizedType) {
            ParameterizedType pOuterType = (ParameterizedType)declaringType;
            if (pOuterType != null) {
                processOuterClass(pOuterType);
            }
            addParameterMapping(pOuterType);
        }
    }

}