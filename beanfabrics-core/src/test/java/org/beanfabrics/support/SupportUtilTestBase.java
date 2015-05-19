package org.beanfabrics.support;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

public class SupportUtilTestBase {
  static List<Member> listOf(Member... member) {
    List<Member> result = new ArrayList<Member>();
    for (Member m : member) {
      result.add(m);
    }
    return result;
  }

  static Field field(Class<?> cls, String name) {
    try {
      return cls.getDeclaredField(name);
    } catch (SecurityException e) {
      throw new UndeclaredThrowableException(e);
    } catch (NoSuchFieldException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  static Method method(Class<?> cls, String name) {
    try {
      return cls.getMethod(name, new Class[0]);
    } catch (SecurityException e) {
      throw new UndeclaredThrowableException(e);
    } catch (NoSuchMethodException e) {
      throw new UndeclaredThrowableException(e);
    }
  }
}
