/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.beanfabrics.log.Logger;
import org.beanfabrics.log.LoggerFactory;

/**
 * The <code>ReflectionUtil</code> is a utility class for doing reflection.
 * 
 * @author Michael Karneim
 */
public class ReflectionUtil {
    private final static Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);
    private static int callCount = 0;

    public interface FieldFilter {
        boolean accept(Field field);
    }

    public interface MethodFilter {
        boolean accept(Method method);
    }

    /**
     * Returns all declared fields of the given {@link Class} and it's
     * superclasses.
     */
    public static List<Field> getAllFields(Class cls) {
        LinkedList<Field> result = new LinkedList<Field>();
        findAllFields(cls, result);
        return result;
    }

    /**
     * Returns all declared fields of the given {@link Class} and it's
     * superclasses that have the given name and are assignable to the given
     * type.
     * 
     * @param cls the class that owns the returned fields
     * @param fieldName name of the returned fields or <code>null</code> for no
     *            name filtering
     * @param fieldType the generic type of the returned fields or
     *            <code>null</code> for no type filtering
     * @return all declared fields that match the given filter arguments
     */
    public static List<Field> getAllFields(Class cls, final String fieldName, final Class fieldType) {
        LinkedList<Field> result = new LinkedList<Field>();
        findAllFields(cls, result, new FieldFilter() {
            public boolean accept(Field field) {
                return (fieldName == null || fieldName.equals(field.getName())) && (fieldType == null || fieldType.isAssignableFrom(field.getType()));
            }
        });
        return result;
    }

    private static void findAllFields(Class cls, Collection<? super Field> result) {
        findAllFields(cls, result, null);
    }

    private static void findAllFields(Class cls, Collection<? super Field> result, FieldFilter filter) {
        Field[] declFields = cls.getDeclaredFields();
        for (Field f : declFields) {
            if (filter == null || filter.accept(f)) {
                result.add(f);
            }
        }
        Class superCls = cls.getSuperclass();
        if (superCls != null) {
            findAllFields(superCls, result, filter);
        }

        // Ugh! We don't have (non static) fields in interfaces, have we??
        //        Class[] interfaces = cls.getInterfaces();
        //        for (Class i : interfaces) {
        //        	findAllFields( i, result, filter);
        //		  }
    }

    public static List<Field> getAllFields(Class cls, final Class annoType) {
        LinkedList<Field> result = new LinkedList<Field>();
        findAllFields(cls, result, new FieldFilter() {
            public boolean accept(Field field) {
                Annotation anno = field.getAnnotation(annoType);
                return (anno != null);
            }
        });
        return result;
    }

    /**
     * Returns all declared members of the given {@link Class} and it's
     * superclasses.
     * 
     * @param cls the class of which to search the members
     */
    public static List<Member> getAllMembers(Class cls) {
        LinkedList<Member> result = new LinkedList<Member>();
        findAllMethods(cls, result);
        findAllFields(cls, result);
        return result;
    }

    /**
     * Returns all declared methods of the given {@link Class} and it's
     * superclasses.
     * 
     * @param cls the class of which to search the methods
     */
    public static List<Method> getAllMethods(Class cls) {
        LinkedList<Method> result = new LinkedList<Method>();
        findAllMethods(cls, result);
        return result;
    }

    private static void findAllMethods(Class cls, Collection<? super Method> result) {
        Method[] declMethods = cls.getDeclaredMethods();
        for (Method m : declMethods) {
            if (result.contains(m) == false) {
                result.add(m);
            }
        }
        Class superCls = cls.getSuperclass();
        if (superCls != null) {
            findAllMethods(superCls, result);
        }
        Class[] interfaces = cls.getInterfaces();
        for (Class i : interfaces) {
            findAllMethods(i, result);
        }
    }

    /**
     * Returns all declared methods of the given {@link Class} and it's
     * superclasses that have the given name and have a return type assignable
     * to the given type.
     * 
     * @param cls the class that owns the returned methods
     * @param methodName name of the returned methods or <code>null</code> for
     *            no name filtering
     * @param parameterTypes the parameter types of the returned methods or
     *            <code>null</code> for no parameter types filtering
     * @param returnType the generic type of the returned methods or
     *            <code>null</code> for no type filtering
     * @return all declared methods that match the given filter arguments
     */
    public static Collection<Method> getAllMethods(Class cls, final String methodName, final Class[] parameterTypes, final Class returnType) {
        LinkedList<Method> result = new LinkedList<Method>();
        findAllMethods(cls, result, new MethodFilter() {
            public boolean accept(Method method) {
                return (methodName == null || methodName.equals(method.getName())) && (returnType == null || returnType.isAssignableFrom(method.getReturnType()))
                        && (parameterTypes == null || Arrays.equals(parameterTypes, method.getParameterTypes()));
            }
        });
        return result;
    }

    /**
     * Returns all Methods of the given {@link Class} and it's superclasses
     * which are annotated with the given <code>annotationType</code>.
     * 
     * @param cls the class of which to search the methods
     * @param annotationType the annotation type which mark methods to be
     *            contained in the result
     */
    public static List<Method> getAllMethods(Class cls, Class annotationType) {
        LinkedList<Method> result = new LinkedList<Method>();
        findAllMethods(cls, annotationType, result);
        return result;
    }

    private static void findAllMethods(Class cls, Collection<? super Method> result, MethodFilter filter) {
        Method[] declMethods = cls.getDeclaredMethods();
        for (Method f : declMethods) {
            if (filter == null || filter.accept(f)) {
                result.add(f);
            }
        }
        Class superCls = cls.getSuperclass();
        if (superCls != null) {
            findAllMethods(superCls, result, filter);
        }
        Class[] interfaces = cls.getInterfaces();
        for (Class i : interfaces) {
            findAllMethods(i, result, filter);
        }
    }

    private static void findAllMethods(Class cls, Class annoType, Collection<? super Method> result) {
        Method[] declMethods = cls.getDeclaredMethods();
        for (Method m : declMethods) {
            Annotation anno = m.getAnnotation(annoType);
            if (anno != null && result.contains(m) == false) {
                result.add(m);
            }
        }
        Class superCls = cls.getSuperclass();
        if (superCls != null) {
            findAllMethods(superCls, annoType, result);
        }
        Class[] interfaces = cls.getInterfaces();
        for (Class i : interfaces) {
            findAllMethods(i, annoType, result);
        }
    }

    /**
     * Returns a {@link List} of all superclasses and all interfaces of the
     * given baseclass, including the baseclass.
     * 
     * @param baseClass
     * @return List
     */
    public static List<Class> getAllClasses(Class baseClass) {
        LinkedList<Class> result = new LinkedList<Class>();
        findAllClasses(baseClass, result);
        return result;
    }

    private static void findAllClasses(Class baseClass, LinkedList<Class> result) {
        if (result.contains(baseClass) == false) {
            result.add(baseClass);
            Class superCls = baseClass.getSuperclass();
            if (superCls != null) {
                findAllClasses(superCls, result);
            }
            Class[] interfaces = baseClass.getInterfaces();
            for (Class i : interfaces) {
                findAllClasses(i, result);
            }
        }
    }

    /**
     * Calls all methods of <code>target</code> that are annotated with the
     * specified <code>annotationType</code>.
     * 
     * @param target the <code>Object</code> to call the methods from
     * @param annotationType the type of the annotation marking the methods to
     *            call
     */
    public static void callAnnotatedMethods(Object target, Class annotationType) {
        Collection<Method> methods = getAllMethods(target.getClass());
        for (Method m : methods) {
            Annotation anno = m.getAnnotation(annotationType);
            if (anno != null) {
                try {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                    m.invoke(target, (Object[])null);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static void setProperties(Object bean, Properties props)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        Class beanClass = bean.getClass();
        for (Object key : props.keySet()) {
            if (key instanceof String) {
                String name = (String)key;
                String value = props.getProperty(name);
                String setterMethodName = "set" + firstCharToUpperCase(name);
                Method m = beanClass.getMethod(setterMethodName, new Class[] { String.class });
                m.invoke(bean, new Object[] { value });
            }
        }
    }

    private static String firstCharToUpperCase(String text) {
        String result = Character.toUpperCase(text.charAt(0)) + text.substring(1);
        return result;
    }

    public static void setFieldValue(Object owner, Field field, Object value)
        throws IllegalArgumentException, IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(owner, value);
    }

    public static Object getFieldValue(Object owner, Field f)
        throws IllegalArgumentException, IllegalAccessException {
        if (!f.isAccessible()) {
            f.setAccessible(true);
        }
        Object result = f.get(owner);
        return result;
    }

    public static Object invokeMethod(Object owner, Method m, Object... args)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        callCount++;
        if (!m.isAccessible()) {
            m.setAccessible(true);
        }
        if (LOG.isDebugEnabled()) {
            if (m.getParameterTypes().length == 0) {
                LOG.debug("Calling " + format(m) + " on " + owner);
            } else if (args == null) {
                LOG.debug("Calling " + format(m) + " on " + owner + " with no arguments");
            } else {
                LOG.debug("Calling " + format(m) + " on " + owner + " with arguments: " + format(args));
            }
        }
        Object result = m.invoke(owner, args);
        return result;
    }

    private static String format(Object[] args) {
        if (args == null) {
            throw new IllegalArgumentException("args==null");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(args[i]);
        }
        return sb.toString();
    }

    private static String format(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getName());
        sb.append("(");
        int count = 0;
        for (Class paramType : m.getParameterTypes()) {
            if (count > 0) {
                sb.append(", ");
            }
            sb.append(paramType.getName());
            count++;
        }
        sb.append(")");
        return sb.toString();
    }

    public static <T> T newInstance(Class<T> cls)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, InstantiationException {
        Constructor<T> constr = cls.getConstructor((Class[])null);
        if (!constr.isAccessible()) {
            constr.setAccessible(true);
        }
        T result = constr.newInstance((Object[])null);
        return result;
    }

}