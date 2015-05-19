package org.beanfabrics.support;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

public class SupportUtil_6_Test extends SupportUtilTestBase {

  private static interface X {
    int x();
  }
  
  private abstract static class A implements X {
    @SuppressWarnings("unused")
    int a;
  }

  private abstract static class B extends A {
    @SuppressWarnings("unused")
    int b;
  }
  
  private abstract static class C extends B implements X {
    @SuppressWarnings("unused")
    int c;
  }
  

  @Test
  public void testSortMembers_AlreadySorted() {
    // Given:
    Field a = field(A.class, "a");
    Field b = field(B.class, "b");
    Field c = field(C.class, "c");
    Method x = method(X.class, "x");
    List<Member> members = listOf(x, a, b, c);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("x is first", x, act.get(0));
    assertEquals("a is second", a, act.get(1));
    assertEquals("b is third", b, act.get(2));
    assertEquals("c is fourth", c, act.get(3));
  }

  @Test
  public void testSortMembers_WrongOrder() {
    // Given:
    Field a = field(A.class, "a");
    Field b = field(B.class, "b");
    Field c = field(C.class, "c");
    Method x = method(X.class, "x");
    List<Member> members = listOf(c, b, a, x);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("x is first", x, act.get(0));
    assertEquals("a is second", a, act.get(1));
    assertEquals("b is third", b, act.get(2));
    assertEquals("c is fourth", c, act.get(3));
  }


}
