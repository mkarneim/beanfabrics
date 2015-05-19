package org.beanfabrics.support;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

import org.junit.Test;

public class SupportUtil_4_Test extends SupportUtilTestBase {

  private static class C1 {
    @SuppressWarnings("unused")
    int z;
  }

  private static class C2 extends C1 {
    @SuppressWarnings("unused")
    int b;
  }

  @Test
  public void testSortMembers_AlreadySorted() {
    // Given:
    Field z = field(C1.class, "z");
    Field b = field(C2.class, "b");
    List<Member> members = listOf(z, b);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("z is first", z, act.get(0));
    assertEquals("b is second", b, act.get(1));
  }

  @Test
  public void testSortMembers_WrongOrder() {
    // Given:
    Field z = field(C1.class, "z");
    Field b = field(C2.class, "b");
    List<Member> members = listOf(b, z);

    // When:
    List<Member> act = SupportUtil.sortMembers(members);

    // Then:
    assertEquals("z is first", z, act.get(0));
    assertEquals("b is second", b, act.get(1));
  }


}
