/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.util;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class IntervalTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IntervalTest.class);
    }

    public IntervalTest() {
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
        new Interval(1, 20);
    }

    @Test
    public void create2() {
        int[] indices = new int[] { 5, 1, 3, 2 };
        Interval[] intervals = Interval.createIntervals(indices);
        assertEquals("intervals.length", 2, intervals.length);
    }

    @Test
    public void create3() {
        int[] indices = new int[] { 40, 20, 10, 41, 42, 43, 44, 15, 14, 13, 12, 11, 21, 22, 23, 25, 24 };
        Interval[] intervals = Interval.createIntervals(indices);
        assertEquals("intervals.length", 3, intervals.length);
    }

    @Test
    public void intersects() {
        Interval a = new Interval(13, 14);
        Interval b = new Interval(9, 10);
        assertEquals("a.intersects(b)", false, a.intersects(b));
        assertEquals("b.intersects(a)", false, b.intersects(a));
    }

    @Test
    public void intersects2() {
        Interval a = new Interval(13, 14);
        Interval b = new Interval(14, 15);
        assertEquals("a.intersects(b)", true, a.intersects(b));
        assertEquals("b.intersects(a)", true, b.intersects(a));
    }

    @Test
    public void intersects3() {
        Interval a = new Interval(13, 14);
        Interval b = new Interval(12, 15);
        assertEquals("a.intersects(b)", true, a.intersects(b));
        assertEquals("b.intersects(a)", true, b.intersects(a));
    }

    @Test
    public void intersects4() {
        Interval a = new Interval(9, 10);
        Interval b = new Interval(10, 12);
        assertEquals("a.intersects(b)", true, a.intersects(b));
        assertEquals("b.intersects(a)", true, b.intersects(a));
    }

    @Test
    public void intersectsCollection() {
        List<Interval> list = new LinkedList<Interval>();
        list.add(new Interval(1, 5));
        list.add(new Interval(6, 9));
        list.add(new Interval(10, 12));
        Interval a = new Interval(13, 14);
        assertEquals("a.intersects(listCell)", false, a.intersects(list));
        Interval b = new Interval(9, 10);
        assertEquals("a.intersects(listCell)", true, b.intersects(list));
    }
}