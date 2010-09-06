/**
 * 
 */
package org.beanfabrics.util;

import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class GenericType {
    private final VariableMapping mapping;
    private final Type type;

    public GenericType(VariableMapping aMapping, Type aType) {
        super();
        this.mapping = aMapping;
        this.type = aMapping.tryResolve(aType);
    }

    public GenericType(Class<?> cls) {
        this(new VariableMapping(null), cls);
    }

    public Type getType() {
        return type;
    }

    public GenericType getTypeParameter(TypeVariable<? extends Class<?>> variable) {
        if (type instanceof Class<?>) {
            Class<?> cls = (Class<?>)type;
            GenericType result = getTypeParameterForClass(mapping, cls, variable);
            return result;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType)type;
            GenericType result = getTypeParameterForParameterizedType(mapping, pType, variable);
            return result;
        } else {
            throw new IllegalStateException();
        }
    }

    public GenericType getFieldType(String fieldname) {
        if (type instanceof ParameterizedType) {
            Class<?> rawType = (Class<?>)((ParameterizedType)type).getRawType();
            GenericType result = getFieldType(mapping, rawType, fieldname);
            return result;
        } else if (type instanceof Class<?>) {
            Class<?> rawType = (Class<?>)type;
            GenericType result = getFieldType(mapping, rawType, fieldname);
            return result;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns the GenericType of the this types method with the given name
     * without method parameters.
     * 
     * @param methodName
     * @return
     */
    public GenericType getMethodReturnType(String methodName) {
        if (type instanceof ParameterizedType) {
            Class<?> rawType = (Class<?>)((ParameterizedType)type).getRawType();
            GenericType result = getMethodReturnType(mapping, rawType, methodName);
            return result;
        } else if (type instanceof Class<?>) {
            Class<?> rawType = (Class<?>)type;
            GenericType result = getMethodReturnType(mapping, rawType, methodName);
            return result;
        } else {
            throw new IllegalStateException();
        }
    }

    public Type tryResolve(Type type) {
        if (type instanceof TypeVariable<?>) {
            return tryResolve((TypeVariable<?>)type);
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
            return (Class<?>)aType;
        } else if (aType instanceof ParameterizedType) {
            return ((ParameterizedType)aType).getRawType();
        } else if (aType instanceof TypeVariable<?> || aType instanceof WildcardType) {
            Type[] bounds;
            if (aType instanceof WildcardType) {
                bounds = ((WildcardType)aType).getUpperBounds();
            } else {
                bounds = ((TypeVariable<?>)aType).getBounds();
            }
            Class<?> result = null;
            for (Type bound : bounds) {
                if (bound instanceof TypeVariable<?>) {
                    Type resolvedBound = tryResolve((TypeVariable<?>)bound);
                    if (resolvedBound != null) {
                        bound = resolvedBound;
                    }
                }
                bound = narrow(bound, targetType);
                if (bound instanceof Class<?>) {
                    Class<?> cls = (Class<?>)bound;
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

    private GenericType getTypeParameterForClass(VariableMapping currentMapping, Class<?> forClass, TypeVariable<?> var) {
        if (var.getGenericDeclaration().equals(forClass)) {
            // Break-Condition
            GenericType result = new GenericType(currentMapping, var);
            return result;
        }
        if (isAssignable(var.getGenericDeclaration(), forClass)) {
            // process Superclass
            {
                Type aSuperType = forClass.getGenericSuperclass();
                if (aSuperType != null && isAssignable(var.getGenericDeclaration(), aSuperType)) {
                    GenericType result = getTypeParameterForType(currentMapping, aSuperType, var);
                    if (result != null) {
                        return result;
                    }
                }
            }

            // process Superinterfaces
            Type[] superInterfaceTypes = forClass.getGenericInterfaces();
            for (Type aSuperType : superInterfaceTypes) {
                if (isAssignable(var.getGenericDeclaration(), aSuperType)) {
                    GenericType result = getTypeParameterForType(currentMapping, aSuperType, var);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private GenericType getTypeParameterForParameterizedType(VariableMapping currentMapping, ParameterizedType forParameterizedType, TypeVariable<?> var) {
        ///////////////////
        //currentMapping = currentMapping.createMapping(forParameterizedType);
        Class<?> rawType = (Class<?>)forParameterizedType.getRawType();
        return getTypeParameterForClass(currentMapping, rawType, var);
    }

    private boolean isAssignable(GenericDeclaration genericDeclaration, Type aType) {
        if (aType == null) {
            return false;
        }
        if (aType instanceof Class<?>) {
            return isAssignable(genericDeclaration, (Class<?>)aType);
        } else if (aType instanceof ParameterizedType) {
            return isAssignable(genericDeclaration, (Class<?>)((ParameterizedType)aType).getRawType());
        } else {
            throw new UnsupportedOperationException("aType=" + aType.getClass().getName());
        }
    }

    private boolean isAssignable(GenericDeclaration genericDeclaration, Class<?> forClass) {
        if (genericDeclaration instanceof Class<?>) {
            Class<?> genClass = (Class<?>)genericDeclaration;
            return genClass.isAssignableFrom(forClass);
        }
        return false;
    }

    private GenericType getTypeParameterForType(VariableMapping currentMapping, Type forSuperType, TypeVariable<?> var)
        throws AssertionError {
        if (forSuperType instanceof Class<?>) {
            GenericType result = getTypeParameterForClass(currentMapping, (Class<?>)forSuperType, var);
            if (result != null) {
                return result;
            }
        } else if (forSuperType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)forSuperType;
            currentMapping = currentMapping.createMapping(parameterizedType);
            GenericType result = getTypeParameterForParameterizedType(currentMapping, parameterizedType, var);
            if (result != null) {
                return result;
            }
        } else {
            throw new AssertionError("Unexpected superType: " + forSuperType);
        }
        return null;
    }

    private static GenericType getFieldType(VariableMapping currentMapping, Class<?> startClass, String fieldname) {
        Type type = null;
        Field field = null; // z.B. BClass<String, Integer> bObject;
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
                // Extend Mapping by Superclass
                Type superType = ownerClass.getGenericSuperclass();
                if (superType instanceof ParameterizedType) {
                    ParameterizedType parameterizedSuperType = (ParameterizedType)superType;
                    currentMapping = new VariableMapping(currentMapping, parameterizedSuperType);
                    ownerClass = (Class<?>)parameterizedSuperType.getRawType();
                } else if (superType instanceof Class<?>) {
                    ownerClass = (Class<?>)superType;
                    currentMapping = new VariableMapping(currentMapping);
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        type = field.getGenericType(); // z.B. BClass<String, Integer>

        // try to resolve type 
        type = currentMapping.tryResolve(type);

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            currentMapping = new VariableMapping(currentMapping, parameterizedType); // z.B. <X1, X2 extends Number> => <String, Integer>
        } else if (type instanceof Class<?>) {
            // We can't go any further
        } else if (type instanceof TypeVariable<?>) {
            // We can't go any further
        } else {
            throw new IllegalStateException("Type not supported so far: " + type.getClass().getName());
        }

        if (type != null) {
            return new GenericType(currentMapping, type);
        } else {
            throw new IllegalStateException();
        }
    }

    private static GenericType getMethodReturnType(VariableMapping currentMapping, Class<?> startClass, String methodName) {
        Type type = null;
        Method method = null;
        {
            Class<?> ownerClass = startClass;
            findmethod: while (true) {
                Method[] methods = ownerClass.getDeclaredMethods();
                for (Method m : methods) {
                    if (m.getName().equals(methodName) && (m.getParameterTypes() == null || m.getParameterTypes().length == 0)) {
                        method = m;
                        break findmethod;
                    }
                }
                // Extend Mapping by Superclass
                Type superType = ownerClass.getGenericSuperclass();
                if (superType instanceof ParameterizedType) {
                    ParameterizedType parameterizedSuperType = (ParameterizedType)superType;
                    currentMapping = new VariableMapping(currentMapping, parameterizedSuperType);
                    ownerClass = (Class<?>)parameterizedSuperType.getRawType();
                } else if (superType instanceof Class<?>) {
                    ownerClass = (Class<?>)superType;
                    currentMapping = new VariableMapping(currentMapping);
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        type = method.getGenericReturnType();

        // try to resolve type 
        type = currentMapping.tryResolve(type);

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            currentMapping = new VariableMapping(currentMapping, parameterizedType); // z.B. <X1, X2 extends Number> => <String, Integer>
        } else if (type instanceof Class<?>) {
            // We can't go any further
        } else if (type instanceof TypeVariable<?>) {
            // We can't go any further
        } else {
            throw new IllegalStateException("Type not supported so far: " + type.getClass().getName());
        }

        if (type != null) {
            return new GenericType(currentMapping, type);
        } else {
            throw new IllegalStateException();
        }

    }

}