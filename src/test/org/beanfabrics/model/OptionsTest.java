/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.event.OptionsEvent;
import org.beanfabrics.event.OptionsListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class OptionsTest {
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(OptionsTest.class);
	}

	public OptionsTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Test
	public void create() {
		new Options();
	}

	@Test
	public void put() {
		Options<Integer> opt = new Options<Integer>();
		opt.put(1, "one");
		assertEquals("opt.size()", 1, opt.size());
		assertEquals("opt.get(1)", "one", opt.get(1));
	}

	@Test
	public void putNullKey() {
		Options<Integer> opt = new Options<Integer>();
		opt.put(null, "nothing");
		assertEquals("opt.size()", 1, opt.size());
		assertEquals("opt.get(null)", "nothing", opt.get(null));
	}

	@Test
	public void putNullValue() {
		Options<Integer> opt = new Options<Integer>();
		try {
			opt.put(1,null);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// ok.
		}
	}

	@Test
	public void putAllWithNullValue() {
		Options<Integer> opt = new Options<Integer>();
		HashMap<Integer,String> map = new HashMap<Integer, String>();
		map.put(1, "one");
		map.put(2, "two");
		map.put(0, null);

		try {
			opt.putAll(map);
			fail("expected IllegalArgumentException");
		} catch ( IllegalArgumentException ex) {
			// ok.
		}
	}

	@Test
	public void putMore() {
		Options<Integer> opt = new Options<Integer>();
		opt.put(null, "nothing");
		assertEquals("opt.size()", 1, opt.size());
		assertEquals("opt.get(null)", "nothing", opt.get(null));

		opt.put(1, "one");
		assertEquals("opt.size()", 2, opt.size());
		assertEquals("opt.get(1)", "one", opt.get(1));

		opt.put(2, "two");
		assertEquals("opt.size()", 3, opt.size());
		assertEquals("opt.get(null)", "nothing", opt.get(null));
		assertEquals("opt.get(1)", "one", opt.get(1));
		assertEquals("opt.get(2)", "two", opt.get(2));
	}

	@Test
	public void putDuplicateValues() {
		Options<Integer> opt = new Options<Integer>();
		opt.put(1, "car");
		assertEquals("opt.size()", 1, opt.size());
		assertEquals("opt.get(1)", "car", opt.get(1));
		try {
			opt.put(2, "car");
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// ok.
		}
	}

	@Test
	public void putAll() {
		Options<Integer> opt = new Options<Integer>();
		HashMap all = new HashMap();
		for (int i = 0; i < 10; ++i) {
			all.put(i, "value " + i);
		}

		opt.putAll(all);
		assertEquals("opt.size()", 10, opt.size());
		for (int i = 0; i < 10; ++i) {
			assertEquals("", "value " + i, opt.get(i));
		}
	}

	@Test
	public void putAll2() {
		Options<Integer> opt = new Options<Integer>();
		HashMap all = new HashMap();
		for (int i = 0; i < 10; ++i) {
			all.put(i, "value " + i);
		}

		opt.putAll(all);

		HashMap all2 = new HashMap();
		for (int i = 5; i < 15; ++i) {
			all2.put(i, "value " + i);
		}

		Counter counter = new Counter();
		opt.addOptionsListener(counter);
		opt.putAll(all2);

		assertEquals("opt.size()", 15, opt.size());
		assertEquals("counter.changed", 1, counter.changed);
		for (int i = 0; i < 15; ++i) {
			assertEquals("", "value " + i, opt.get(i));
		}
	}

	@Test
	public void putAllDuplicateValues() {
		Options<Integer> opt = new Options<Integer>();

		opt.put(10, "value 0");

		HashMap all = new HashMap();
		for (int i = 0; i < 10; ++i) {
			all.put(i, "value " + i);
		}
		try {
			opt.putAll(all);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// ok.
		}
	}

	@Test
	public void clear() {
		Options<Integer> opt = new Options<Integer>();
		opt.put(1, "one");
		opt.put(2, "two");

		Counter counter = new Counter();
		opt.addOptionsListener(counter);

		opt.clear();
		assertEquals("opt.size()", 0, opt.size());
		assertEquals("counter.changed", 1, counter.changed);
	}

	@Test
	public void remove() {
		Options<Integer> opt = new Options<Integer>();
		opt.put(1, "one");

		Counter counter = new Counter();
		opt.addOptionsListener(counter);

		opt.remove(1);
		assertEquals("opt.size()", 0, opt.size());
		assertEquals("counter.changed", 1, counter.changed);
		assertEquals("opt.get(1)", null, opt.get(1));
	}

	@Test
	public void removeAll() {
		Options<Integer> opt = new Options<Integer>();

		opt.put(1, "one");
		opt.put(2, "two");
		opt.put(3, "three");
		opt.put(4, "four");
		assertEquals("opt.size()", 4, opt.size());

		Set<Integer> remove = new HashSet<Integer>();
		remove.add(1);
		remove.add(2);
		remove.add(4);

		Counter counter = new Counter();
		opt.addOptionsListener(counter);

		opt.removeAll(remove);
		assertEquals("opt.size()", 1, opt.size());
		assertEquals("counter.changed", 1, counter.changed);
		assertEquals("opt.get(1)", null, opt.get(1));
		assertEquals("opt.get(2)", null, opt.get(2));
		assertEquals("opt.get(3)", "three", opt.get(3));
		assertEquals("opt.get(4)", null, opt.get(4));
	}

	@Test
	public void getKey() {
		String ONE = "one";
		String TWO = "two";
		String THREE = "three";

		Options<Integer> opt = new Options<Integer>();
		opt.put(1, ONE);
		opt.put(2, TWO);
		opt.put(3, THREE);

		assertEquals("opt.getKey(THREE)", 3, opt.getKey(THREE));
		assertEquals("opt.getKey(TWO)", 2, opt.getKey(TWO));
		assertEquals("opt.getKey(ONE)", 1, opt.getKey(ONE));

		try {
			opt.getKey("foo");
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException ex) {
			// ok.
		}
	}

	@Test
	public void events() {
		Options<Integer> opt = new Options<Integer>();
		Counter counter = new Counter();
		opt.addOptionsListener(counter);

		assertEquals("counter.changed", 0, counter.changed);

		opt.put(1, "one");
		assertEquals("counter.changed", 1, counter.changed);
		opt.put(2, "two");
		assertEquals("counter.changed", 2, counter.changed);

		HashMap all = new HashMap();
		for (int i = 0; i < 10; ++i) {
			all.put(i, "value " + i);
		}
		opt.putAll(all);
		assertEquals("counter.changed", 3, counter.changed);

		opt.put(1, "one");
		assertEquals("counter.changed", 4, counter.changed);

		opt.remove(1);
		assertEquals("counter.changed", 5, counter.changed);

		opt.remove(1); // no change
		assertEquals("counter.changed", 5, counter.changed);
	}

	private static class Counter implements OptionsListener {
		int changed;

		public void changed(OptionsEvent evt) {
			changed++;
		}
	}
}