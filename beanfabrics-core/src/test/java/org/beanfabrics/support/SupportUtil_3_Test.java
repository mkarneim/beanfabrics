package org.beanfabrics.support;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

import org.junit.Test;

public class SupportUtil_3_Test extends SupportUtilTestBase {

  private static class C1 {
    @SuppressWarnings("unused")
    int a;
  }

  private static class C2 {
    @SuppressWarnings("unused")
    int b;
  }

  @Test
  public void testSortMembers_AlreadySorted() {
    // Given:
    Field a = field(C1.class, "a");
    Field b = field(C2.class, "b");
    List<Member> members = listOf(a, b);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("a is first", a, act.get(0));
    assertEquals("b is second", b, act.get(1));
  }

  @Test
  public void testSortMembers_WrongOrder() {
    // Given:
    Field a = field(C1.class, "a");
    Field b = field(C2.class, "b");
    List<Member> members = listOf(b, a);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("a is first", a, act.get(0));
    assertEquals("b is second", b, act.get(1));
  }


}
