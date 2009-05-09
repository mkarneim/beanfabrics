/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class SelectionTest {
	private final int ELEMENT_SIZE = 10;

	private IListPM<DummyPM> list;
	private Selection<DummyPM> selection;

	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SelectionTest.class);
    }

    public SelectionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.junit.Before
    public void setUp() {
    	MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
    	Selection<DummyPM> sel = map.getSelection();
    	assertNotNull("sel", sel);
    	for( int i=0; i<ELEMENT_SIZE; ++i) {
    		DummyPM elem = new DummyPM();
    		elem.name.setText("name "+i);
    		map.put( i, elem);
    	}

    	this.list = map;
    	this.selection = sel;
    }



    @Test
    public void add() {
    	selection.add( list.getAt(3));
    	assertEquals("selection.size()", 1, selection.size());
    }

    @Test
    public void addAllFromProperty() {
    	selection.addAll();
    	assertEquals("selection.size()", list.size(), selection.size());
    }

    @Test
    public void addAll() {
    	selection.addAll( list.toCollection());
    	assertEquals("selection.size()", list.size(), selection.size());
    }

    @Test
    public void addInterval() {
    	selection.addInterval(2, 4); // select elements with index 2,3,4
    	assertEquals("selection.size()", 3, selection.size());
    	assertTrue("selection.contains(2)", selection.contains(2));
    	assertTrue("selection.contains(3)", selection.contains(3));
    	assertTrue("selection.contains(4)", selection.contains(4));
    }

    @Test
    public void clear() {
    	selection.addAll();
    	assertEquals("selection.size()", list.size(), selection.size());

    	selection.clear();
    	assertEquals("selection.size()", 0, selection.size());
    }

    @Test
    public void containsIndex() {
    	selection.addInterval(2, 4); // select elements with index 2,3,4
    	int index = 2;
    	assertEquals("selection.contains(index)", true, selection.contains(index));
    	index = 8;
    	assertEquals("selection.contains(index)", false, selection.contains(index));
    }

    @Test
    public void containsObject() {
    	selection.addInterval(3, 3);
    	Object obj = list.getAt(3);
    	assertEquals("selection.contains(obj)", true, selection.contains(obj));
    	obj = list.getAt(4);
    	assertEquals("selection.contains(obj)", false, selection.contains(obj));
    }

    @Test
    public void containsAll() {
    	Collection col = list.toCollection();
    	selection.addAll(col);
    	assertEquals("selection.containsAll(col)", true, selection.containsAll(col));
    	selection.clear();
    	assertEquals("selection.isEmpty()", true, selection.isEmpty());
    	assertEquals("selection.containsAll(col)", false, selection.containsAll(col));
    }

    @Test
    public void getFirstIndex() {
    	selection.addInterval(3, 6);
    	selection.addInterval(2, 2);
    	assertEquals("selection.getFirstIndex()", 2, selection.getMinIndex());

    	selection.clear();
    	assertEquals("selection.getFirstIndex()", -1, selection.getMinIndex());
    }

    @Test
    public void isEmpty() {
    	assertEquals("selection.isEmpty()", true, selection.isEmpty());

    	selection.addInterval(3, 6);
    	assertEquals("selection.isEmpty()", false, selection.isEmpty());

    	selection.clear();
    	assertEquals("selection.isEmpty()", true, selection.isEmpty());
    }

    @Test
    public void iterator() {
    	Iterator<DummyPM> it = selection.iterator();
    	assertNotNull("it", it);
    	assertEquals("it.hasNext()", false, it.hasNext());

    	selection.addAll();
    	it = selection.iterator();
    	assertNotNull("it", it);
    	assertEquals("it.hasNext()", true, it.hasNext());
    	HashSet elementsInIterator = new HashSet();
    	for( int i=0; i<list.size(); ++i) {
    		assertEquals("it.hasNext()", true, it.hasNext());
    		DummyPM elem = it.next();
    		assertNotNull("elem", elem);
    		elementsInIterator.add(elem);
    	}
    	assertEquals("elementsInIterator.size()", list.size(), elementsInIterator.size());

    	Collection allElems = list.toCollection();
    	allElems.removeAll(elementsInIterator);
    	assertEquals("allElems.isEmpty()", true, allElems.isEmpty());

    	allElems = list.toCollection();
    	allElems.retainAll(elementsInIterator);
    	assertEquals("elementsInIterator.size()", allElems.size(), elementsInIterator.size());
    }

    @Test
    public void iteratorRemove() {
    	Iterator<DummyPM> it = selection.iterator();
    	assertNotNull("it", it);
    	assertEquals("it.hasNext()", false, it.hasNext());

    	selection.addAll();
    	it = selection.iterator();
    	assertNotNull("it", it);
    	assertEquals("it.hasNext()", true, it.hasNext());
    	while( it.hasNext()) {
    		DummyPM elem = it.next();
    		assertEquals("selection.contains(elem)", true, selection.contains(elem));
    		it.remove();
    		assertEquals("selection.contains(elem)", false, selection.contains(elem));
    	}
    }

    @Test
    public void remove() {
    	selection.addAll();
    	Object obj;
    	selection.remove(list.getAt(4));
    	assertEquals("selection.contains(4)", false, selection.contains(4));

    }

    @Test
    public void removeAll() {
    	selection.addAll();
    	Collection col = list.toCollection();
    	selection.removeAll(col);
    	assertEquals("selection.isEmpty()", true, selection.isEmpty());
    }

    @Test
    public void removeInterval() {
    	selection.addAll();
    	selection.removeInterval(3,6);
    	assertEquals("selection.size()", 6, selection.size());
    	for( int i=0; i<3;++i) {
    		assertEquals("selection.contains("+i+")", true, selection.contains(i));
    	}
    	for( int i=3; i<=6;++i) {
    		assertEquals("selection.contains("+i+")", false, selection.contains(i));
    	}
    	for( int i=7; i<list.size();++i) {
    		assertEquals("selection.contains("+i+")", true, selection.contains(i));
    	}
    }

    @Test
    public void retainAll() {
    	selection.addAll();
    	assertEquals("selection.size()", list.size(), selection.size());
    	Collection col = list.toCollection();
    	assertEquals("col.size()", list.size(), col.size());
    	col.remove( list.getAt(2));
    	col.remove( list.getAt(3));
    	assertEquals("col.size()", list.size()-2, col.size());


    	selection.retainAll(col);
    	assertEquals("selection.size()", list.size()-2, selection.size());
    }

    @Test
    public void size() {
    	assertEquals("selection.size()", 0, selection.size());
    	selection.addAll();
    	assertEquals("selection.size()", list.size(), selection.size());
    }

    @Test
    public void toArray() {
    	selection.addAll();
    	Object[] array = selection.toArray();
    	assertEquals("array.length", list.size(), array.length);
    	for( int i=0; i<array.length; ++i) {
    		assertEquals("array[i]",list.getAt(i), array[i]);
    	}
    }

    @Test
    public void genericToArray() {
    	selection.addAll();
    	DummyPM[] array = selection.toArray(new DummyPM[list.size()]);
    	assertEquals("array.length", list.size(), array.length);
    	for( int i=0; i<array.length; ++i) {
    		assertEquals("array[i]",list.getAt(i), array[i]);
    	}
    }

    private static class DummyPM extends AbstractPM {
    	private TextPM name = new TextPM();

    	public DummyPM() {
    		PMManager.setup(this);
    	}
    }
}