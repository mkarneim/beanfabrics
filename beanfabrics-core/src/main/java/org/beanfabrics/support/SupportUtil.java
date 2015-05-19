/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org Use is subject to license terms. See
 * license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Karneim
 */
public class SupportUtil {
  private static class Entry<M extends Member> {
    private final M member;
    private final int level;

    public Entry(M member, int level) {
      this.member = member;
      this.level = level;
    }

    public int getLevel() {
      return level;
    }

    public M getMember() {
      return member;
    }

    @Override
    public String toString() {
      return "Entry [member=" + member + ", level=" + level + "]";
    }
  }

  /**
   * Returns a sorted copy of the given {@link List} of {@link Member}s. The members are sorted by the hierarchical
   * level of their declaring {@link Class} or by the value of the optional defined {@link SortOrder} annotation, if the
   * compared members are declared in the same class.
   * 
   * @param members the members to sort
   * @return the sorted list
   */
  public static <M extends Member> List<M> sortMembers(List<M> members) {
    List<Entry<M>> entries = toEntries(members);

    Comparator<Entry<M>> comparator = new Comparator<Entry<M>>() {
      public int compare(Entry<M> o1, Entry<M> o2) {
        int levelComparison = o1.getLevel() - o2.getLevel();
        if (levelComparison != 0) {
          return levelComparison;
        }
        int classnameComparison =
            o1.getMember().getDeclaringClass().getName().compareTo(o2.getMember().getDeclaringClass().getName());
        if (classnameComparison != 0) {
          return classnameComparison;
        }

        int sortOrderComparison = getSortOrderComparison(o1.getMember(), o2.getMember());
        if (sortOrderComparison != 0) {
          return sortOrderComparison;
        }

        int memberNameComparison = o1.getMember().getName().compareTo(o2.getMember().getName());
        return memberNameComparison;
      }

      private int getSortOrderComparison(Member m1, Member m2) {
        SortOrder a1 = getAnnotation(m1, SortOrder.class);
        SortOrder a2 = getAnnotation(m2, SortOrder.class);
        int i1 = a1 == null ? 0 : a1.value();
        int i2 = a2 == null ? 0 : a2.value();
        int sortOrderComparison = i1 - i2;
        return sortOrderComparison;
      }

      private <T extends Annotation> T getAnnotation(Member member, Class<T> cls) {
        if (member instanceof Field) {
          return ((Field) member).getAnnotation(cls);
        } else if (member instanceof Method) {
          return ((Method) member).getAnnotation(cls);
        } else {
          throw new Error("Unexpected member type: " + member.getClass().getName());
        }
      }
    };

    try {
      Collections.sort(entries, comparator);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      throw ex;
    }

    return toMembers(entries);
  }

  private static <M extends Member> List<M> toMembers(List<Entry<M>> entries) {
    List<M> result = new ArrayList<M>();
    for (Entry<M> e : entries) {
      result.add(e.getMember());
    }
    return result;
  }

  private static <M extends Member> List<Entry<M>> toEntries(List<M> members) {
    List<Entry<M>> result = new ArrayList<Entry<M>>();
    Map<Class<?>, Integer> classLevelCache = new HashMap<Class<?>, Integer>();
    for (M member : members) {
      Class<?> cls = member.getDeclaringClass();
      int level = calculateHierarchyLevel(cls, classLevelCache);
      result.add(new Entry<M>(member, level));
    }
    return result;
  }

  private static int calculateHierarchyLevel(Class<?> cls, Map<Class<?>, Integer> classLevelCache) {
    if (cls == null) {
      return 0;
    }
    Integer cachedValue = classLevelCache.get(cls);
    if (cachedValue != null) {
      return cachedValue;
    }
    Class<?> superclass = cls.getSuperclass();
    int result = 1 + calculateHierarchyLevel(superclass, classLevelCache);

    Class<?>[] interfaces = cls.getInterfaces();
    for (Class<?> interf : interfaces) {
      result = Math.max(result, 1 + calculateHierarchyLevel(interf, classLevelCache));
    }
    classLevelCache.put(cls, result);
    return result;
  }

  public static String format(Method method) {
    StringBuffer buf = new StringBuffer();
    buf.append(getBasename(method.getDeclaringClass()));
    buf.append(".").append(method.getName());
    buf.append("(");
    Class<?>[] params = method.getParameterTypes();
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

  private static String getBasename(Class<?> cls) {
    String name = cls.getName();
    int idx = name.lastIndexOf('.');
    if (idx == -1) {
      return name;
    } else {
      return name.substring(idx + 1);
    }
  }

}
