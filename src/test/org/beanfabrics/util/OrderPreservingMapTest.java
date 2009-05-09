/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Max Gensthaler
 */
// TODO (mg) implement all tests
public class OrderPreservingMapTest {
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(OrderPreservingMapTest.class);
	}

	private OrderPreservingMap<String, String> map;
	private String[] keys;
	private String[] values;
	private int mapInitialSize;

	public OrderPreservingMapTest() {
	}

	@Before
	public void setUp() throws Exception {
		map = new OrderPreservingMap<String, String>();
		// non alphanumeric sorted keys and values
		keys = new String[] { "key 1", "key 0" };
		values = new String[] { "value 1", "value 0" };
		map.put(keys[0], values[0]);
		map.put(keys[1], values[1]);
		mapInitialSize = map.size();
	}

	@Test
	public void testPutAll() {
		HashMap<String, String> mapToPut = new HashMap<String, String>();
		for (int i = 5; i < 10; i++) {
			mapToPut.put("key " + i, "value " + i);
		}
		map.putAll(mapToPut);
		assertEquals("map.size()", mapInitialSize + mapToPut.size(), map.size());
		Object[] keys = map.keyArray();
		assertEquals("map.keyArray().length", mapInitialSize + mapToPut.size(), keys.length);
		for (int i = 5; i < 10; i++) {
			assertEquals("map.get()", "value " + i, map.get("key " + i));
		}
	}

	@Test
	public void testPutKV() {
		map.put("key 2", "value 2");
		assertEquals("map.size()", mapInitialSize + 1, map.size());
		assertEquals("map.get()", "value 2", map.get("key 2"));
	}

	@Test
	public void testPutKVInt() {
		map.put("key 2", "value 2", 0);
		assertEquals("map.size()", mapInitialSize + 1, map.size());
		assertEquals("map.get()", "value 2", map.get("key 2"));
	}

	@Test
	public void testKeySet() {
		Set<String> keySet = map.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String key = it.next();
			assertFalse("key: ==", key == keys[0] && key == keys[1]);
			assertTrue("key: String.equals()", key.equals(keys[0]) || key.equals(keys[1]));
		}
	}

	@Test
	public void testKeySetReference() {
		Set<String> keySet = map.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String key = it.next();
			assertTrue("key: ==", key == keys[0] || key == keys[1]);
			assertTrue("key: String.equals()", key.equals(keys[0]) || key.equals(keys[1]));
		}
	}

	@Test
	public void testKeyArray() {
		Object[] keyArray = map.keyArray();
		for (int i = 0; i < keys.length; i++) {
			assertEquals("map.keyArray()["+i+"]", keys[i], keyArray[i]);
		}
	}

	@Test
	public void testOrderedKeys() {
		int i = map.size() - 1;
		for (String key : map.orderedKeys()) {
			assertFalse("map.orderedKeys()[" + i + "] ==", key == keys[0] && key == keys[1]);
			assertEquals("map.ordereredKeys()[" + i + "]", "key " + i, key);
			i--;
		}
	}

	@Test
	public void testOrderedKeysReference() {
		int i = map.size() - 1;
		for (String key : map.orderedKeysReference()) {
			assertTrue("map.orderedKeysReference()[" + i + "] ==", key == keys[0] || key == keys[1]);
			assertEquals("map.orderedKeysReference()[" + i + "]", "key " + i, key);
			i--;
		}
	}

//	@Test
//	public void testEntrySet() {
//	}
//
//	@Test
//	public void testEntrySetReference() {
//	}

	@Test
	public void testValues() {
		int i = 0;
		for (String value : map.values()) {
			assertFalse("map.values()[" + i + "] ==", value == keys[0] && value == keys[1]);
			assertEquals("map.values()[" + i + "]", values[i], value);
			i++;
		}
	}

	@Test
	public void testValuesReference() {
		int i = 0;
		for (String value : map.valuesReference()) {
			assertTrue("map.valuesReference()[" + i + "] ==", value == values[0] || value == values[1]);
			assertEquals("map.valuesReference()[" + i + "]", values[i], value);
			i++;
		}
	}

	@Test
	public void testGetObject() {
		for (int i = 0; i < keys.length; i++) {
			assertEquals("map.get(key["+i+"])", values[i], map.get(keys[i]));
		}
	}

	@Test
	public void testGetInt() {
		for (int i = 0; i < keys.length; i++) {
			assertEquals("map.get("+i+")", values[i], map.get(i));
		}
	}

	@Test
	public void testGetKey() {
		for (int i = 0; i < keys.length; i++) {
			assertEquals("map.getKey("+i+")", keys[i], map.getKey(i));
		}
	}

	@Test
	public void testGetAll() {
		map.put("key 2", "value 2");
		Collection<String> vals = map.getAll(new HashSet<String>(Arrays.asList(keys)));
		assertEquals("map.getAll().size()", mapInitialSize, vals.size());
		for (String value : values) {
			assertTrue("map.getAll().contains()", vals.contains(value));
		}
	}

//	@Test
//	public void testIndexOfKey() {
//	}
//
//	@Test
//	public void testContainsKey() {
//	}
//
//	@Test
//	public void testContainsValue() {
//	}
//
//	@Test
//	public void testContainsAll() {
//	}
//
//	@Test
//	public void testReverseOrder() {
//	}
//
//	@Test
//	public void testReorderCollectionOfK() {
//	}
//
//	@Test
//	public void testReorderKArray() {
//	}
//
//	@Test
//	public void testRemoveObject() {
//	}
//
//	@Test
//	public void testRemoveInt() {
//	}
//
//	@Test
//	public void testRemoveAllKeys() {
//	}
//
//	@Test
//	public void testRetainAllKeys() {
//	}

	@Test
	public void testClear() {
		map.clear();
		assertEquals("map.size()", 0, map.size());
	}

	@Test
	public void testSize() {
		OrderPreservingMap<String, String> map = new OrderPreservingMap<String, String>();
		assertEquals("new OrderPreservingMap().size()", 0, map.size());
		map.put("key 0", "value 0");
		assertEquals("map.size()", 1, map.size());
		map.put("key 0", "value 0.1");
		assertEquals("map.size()", 1, map.size());
		map.put("key 1", "value 1");
		assertEquals("map.size()", 2, map.size());
	}

	@Test
	public void testIsEmpty() {
		OrderPreservingMap<String, String> map = new OrderPreservingMap<String, String>();
		assertTrue("map.isEmpty()", map.isEmpty());
		map.put("key 0", "value 0");
		assertFalse("map.isEmpty()", map.isEmpty());
	}

//	@Test
//	public void testToArrayVArray() {
//	}
//
//	@Test
//	public void testToArray() {
//	}
//
//	@Test
//	public void testIndexOf() {
//	}
//
//	@Test
//	public void testToCollection() {
//	}
//
//	@Test
//	public void testListIterator() {
//	}
//
//	@Test
//	public void testKeyListIterator() {
//	}
}