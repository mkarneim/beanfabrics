/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Michael Karneim
 */
public class SupportUtil {
    /**
     * Sorts the given {@link List} of {@link Method}s according to their
     * declaring {@link Class} and the value of the optional defined
     * {@link SortOrder} annotation.
     * 
     * @param methods the methods to sort
     * @return the sorted list
     */
    public static List<Method> sortMethods(List<Method> methods) {
        Comparator<Method> comparator = new Comparator<Method>() {
            public int compare(Method o1, Method o2) {
                if (o1.getDeclaringClass() == o2.getDeclaringClass()) {
                    SortOrder a1 = o1.getAnnotation(SortOrder.class);
                    SortOrder a2 = o2.getAnnotation(SortOrder.class);
                    int i1 = a1 == null ? 0 : a1.value();
                    int i2 = a2 == null ? 0 : a2.value();
                    return i1 - i2;
                }
                if (o1.getDeclaringClass().isAssignableFrom(o2.getDeclaringClass())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        Collections.sort(methods, comparator);
        return methods;
    }

    /**
     * Sorts the given {@link List} of {@link Field}s according to their
     * declaring {@link Class} and the value of the optional defined
     * {@link SortOrder} annotation.
     * 
     * @param fields the fields to sort
     * @return the sorted list
     */
    public static List<Field> sortFields(List<Field> fields) {
        Comparator<Field> comparator = new Comparator<Field>() {
            public int compare(Field o1, Field o2) {
                if (o1.getDeclaringClass() == o2.getDeclaringClass()) {
                    SortOrder a1 = o1.getAnnotation(SortOrder.class);
                    SortOrder a2 = o2.getAnnotation(SortOrder.class);
                    int i1 = a1 == null ? 0 : a1.value();
                    int i2 = a2 == null ? 0 : a2.value();
                    return i1 - i2;
                }
                if (o1.getDeclaringClass().isAssignableFrom(o2.getDeclaringClass())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        Collections.sort(fields, comparator);
        return fields;
    }

    /**
     * Sorts the given {@link List} of {@link Member}s according to their
     * declaring {@link Class} and the value of the optional defined
     * {@link SortOrder} annotation.
     * 
     * @param members the members to sort
     * @return the sorted list
     */
    public static List<Member> sortMembers(List<Member> members) {
        Comparator<Member> comparator = new Comparator<Member>() {
            public int compare(Member o1, Member o2) {
                if (o1.getDeclaringClass() == o2.getDeclaringClass()) {
                    SortOrder a1 = getAnnotation(o1, SortOrder.class);
                    SortOrder a2 = getAnnotation(o2, SortOrder.class);
                    int i1 = a1 == null ? 0 : a1.value();
                    int i2 = a2 == null ? 0 : a2.value();
                    return i1 - i2;
                }
                if (o1.getDeclaringClass().isAssignableFrom(o2.getDeclaringClass())) {
                    return -1;
                } else {
                    return 1;
                }
            }

            <T extends Annotation> T getAnnotation(Member member, Class<T> cls) {
                if (member instanceof Field) {
                    return ((Field)member).getAnnotation(cls);
                } else if (member instanceof Method) {
                    return ((Method)member).getAnnotation(cls);
                } else {
                    throw new Error("Unexpected member type: " + member.getClass().getName());
                }
            }
        };
        Collections.sort(members, comparator);
        return members;
    }

    public static String format(Method method) {
        StringBuffer buf = new StringBuffer();
        buf.append(getBasename(method.getDeclaringClass()));
        buf.append(".").append(method.getName());
        buf.append("(");
        Class[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(getBasename(params[i]));
        }
        buf.append(")");
        return buf.toString();
    }

    public static String format(Field field) {
        StringBuffer buf = new StringBuffer();
        buf.append(getBasename(field.getDeclaringClass()));
        buf.append(".").append(field.getName());
        return buf.toString();
    }

    private static String getBasename(Class cls) {
        String name = cls.getName();
        int idx = name.lastIndexOf('.');
        if (idx == -1) {
            return name;
        } else {
            return name.substring(idx + 1);
        }
    }

}