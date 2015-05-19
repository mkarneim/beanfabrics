package org.beanfabrics.support;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

import org.junit.Test;

public class SupportUtil_2_Test extends SupportUtilTestBase {

  private static class C1 {
    @SortOrder(2)
    int a;
    @SortOrder(1)
    int b;
  }

  @Test
  public void testSortMembers_AlreadySorted() {
    // Given:
    Field a = field(C1.class, "a");
    Field b = field(C1.class, "b");
    List<Member> members = listOf(a, b);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("b is first", b, act.get(0));
    assertEquals("a is second", a, act.get(1));
  }

  @Test
  public void testSortMembers_WrongOrder() {
    // Given:
    Field a = field(C1.class, "a");
    Field b = field(C1.class, "b");
    List<Member> members = listOf(b, a);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("b is first", b, act.get(0));
    assertEquals("a is second", a, act.get(1));
  }

}
