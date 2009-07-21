/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class PathTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PathTest.class);
    }

    public PathTest() {
    }

    @BeforeClass
    public static void setUpClass()
        throws Exception {
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    @Test
    public void create() {
        // with "this"
        Path p1 = new Path("this.a.b.c");
        assertEquals("p1.toString()", "this.a.b.c", p1.toString());
        assertEquals("p1.length()", 3, p1.length());
        Iterator<String> it1 = p1.iterator();
        assertEquals("it1.next()", "a", it1.next());
        assertEquals("it1.next()", "b", it1.next());
        assertEquals("it1.next()", "c", it1.next());

        // without "this"
        Path p2 = new Path("a.b.c");
        assertEquals("p2.toString()", "a.b.c", p2.toString());
        assertEquals("p2.length()", 3, p2.length());
        Iterator<String> it2 = p1.iterator();
        assertEquals("it2.next()", "a", it2.next());
        assertEquals("it2.next()", "b", it2.next());
        assertEquals("it2.next()", "c", it2.next());

        // The paths are equal
        assertTrue("p1.equals(p2)", p1.equals(p2));
        assertTrue("p1.hashCode() == p2.hashCode()", p1.hashCode() == p2.hashCode());

        // Unequal paths
        Path p3 = new Path("foo.bar");
        assertFalse("p3.equals(p1)", p3.equals(p1));
        assertEquals("p3.getLastElement()", "bar", p3.getLastElement());

        // Ends with dot
        Path p4 = new Path("foo.bar.");
        assertEquals("p4.length()", 3, p4.length());
        assertEquals("p4.getLastElement()", "", p4.getLastElement());

        // "this" ends with dot
        Path p5 = new Path("this.");
        assertEquals("p5.length()", 1, p5.length());
        assertEquals("p5.getLastElement()", "", p5.getLastElement());

        // "this" in the middle
        try {
            new Path("a.this");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected.
        }

        // multible "this"
        try {
            new Path("this.this");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected.
        }
    }

    @Test
    public void length() {
        Path p = new Path("this.a.b.c");
        assertEquals("pM.length()", 3, p.length());
        p = new Path("this.a.b");
        assertEquals("pM.length()", 2, p.length());
        p = new Path("this.a");
        assertEquals("pM.length()", 1, p.length());
        p = new Path("this");
        assertEquals("pM.length()", 0, p.length());
        p = new Path("a.b.c");
        assertEquals("pM.length()", 3, p.length());
        p = new Path("a.b");
        assertEquals("pM.length()", 2, p.length());
        p = new Path("a");
        assertEquals("pM.length()", 1, p.length());

    }

    @Test
    public void getSubPath() {
        Path p = new Path("this.a.b.c");
        Path subpath = p.getSubPath(0, 0);
        assertEquals("subpath.toString()", "this", subpath.toString());
        subpath = p.getSubPath(0, 1);
        assertEquals("subpath.toString()", "this.a", subpath.toString());
        subpath = p.getSubPath(0, 2);
        assertEquals("subpath.toString()", "this.a.b", subpath.toString());
        subpath = p.getSubPath(0, 3);
        assertEquals("subpath.toString()", "this.a.b.c", subpath.toString());
        subpath = p.getSubPath(1, 2);
        assertEquals("subpath.toString()", "b.c", subpath.toString());
        subpath = p.getSubPath(2, 1);
        assertEquals("subpath.toString()", "c", subpath.toString());

        try {
            p.getSubPath(0, -1);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // ok.
        }
        try {
            p.getSubPath(0, 5);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // ok.
        }
    }

    @Test
    public void getSubPathWithoutThis() {
        Path p = new Path("a.b.c");
        Path subpath = p.getSubPath(0, 0);
        assertEquals("subpath.toString()", "this", subpath.toString());
        subpath = p.getSubPath(0, 1);
        assertEquals("subpath.toString()", "a", subpath.toString());
        subpath = p.getSubPath(0, 2);
        assertEquals("subpath.toString()", "a.b", subpath.toString());
        subpath = p.getSubPath(0, 3);
        assertEquals("subpath.toString()", "a.b.c", subpath.toString());
        try {
            p.getSubPath(0, -1);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // ok.
        }
        try {
            p.getSubPath(0, 5);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // ok.
        }
    }

    @Test
    public void getParent() {
        Path path = new Path("this.a.b.c");
        Path parent = path.getParent();
        assertEquals("parent.toString()", "this.a.b", parent.toString());
        parent = parent.getParent();
        assertEquals("parent.toString()", "this.a", parent.toString());
        parent = parent.getParent();
        assertEquals("parent.toString()", "this", parent.toString());
        parent = parent.getParent();
        assertNull("parent", parent);
    }

    @Test
    public void iterate() {
        Path path = new Path("this.a.b.c");
        Iterator<String> it = path.iterator();
        assertEquals("it.hasNext()", true, it.hasNext());
        assertEquals("it.next()", "a", it.next());
        assertEquals("it.hasNext()", true, it.hasNext());
        assertEquals("it.next()", "b", it.next());
        assertEquals("it.hasNext()", true, it.hasNext());
        assertEquals("it.next()", "c", it.next());
        assertEquals("it.hasNext()", false, it.hasNext());
        try {
            it.next();
            fail("expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // ok.
        }
    }

    @Test
    public void concat() {
        Path a = new Path("this.a.b.c");
        Path b = new Path("this.x.y.z");

        assertEquals("Path.concat(a,b)", "this.a.b.c.x.y.z", Path.concat(a, b).toString());
    }

    @Test
    public void concat2() {
        Path a = new Path("a.b.c");
        Path b = new Path("this.x.y.z");

        assertEquals("Path.concat(a,b)", "a.b.c.x.y.z", Path.concat(a, b).toString());
    }
}