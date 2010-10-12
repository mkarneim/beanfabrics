/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListEvent;
import org.beanfabrics.event.ListListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class MapPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MapPMTest.class);
    }

    private static class DummyPM extends AbstractPM {
        IntegerPM id = new IntegerPM();
        TextPM text = new TextPM();

        public DummyPM() {
            String str = Character.toString((char)(Math.random() * 26 + (int)'A'));
            text.setText(str);
            PMManager.setup(this);
        }

        public DummyPM(String id) {
            this();
            this.id.setText(id);
        }

        public String getId() {
            return "id=" + id.getText() + "";
        }

        public void setText(String value) {
            this.text.setText(value);
        }

        public String getText() {
            return text.getText();
        }
    }

    public MapPMTest() {
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
        new MapPM<Integer, DummyPM>();
    }

    @Test
    public void putMany() {
        MapPM<Integer, TextPM> map = new MapPM<Integer, TextPM>();
        TextPM[] elems = new TextPM[10];
        for (int i = 0; i < elems.length; ++i) {
            elems[i] = new TextPM();
            map.put(i, elems[i]);
        }
        assertEquals("map.size()", elems.length, map.size());
        for (int i = 0; i < elems.length; ++i) {
            assertEquals("map.get(i)", elems[i], map.get(i));
            assertEquals("map.get((Integer)i)", elems[i], map.get(i));
        }
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void clear() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.clear();
        assertEquals("map.size()", 0, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void clearWithSelection() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelectedKeys().addAll(map.keySet());
        assertEquals("map.getSelection().size()", NUM, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.clear();
        assertEquals("map.size()", 0, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void clearWithSelectionIntervals() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelectedKeys().addAll(map.keySet());
        map.getSelectedKeys().remove(4); // selected keys are now: 0,1,2,3, ,5,6,7,8,9

        assertEquals("map.getSelection().size()", NUM - 1, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.clear();
        assertEquals("map.size()", 0, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysAdd() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().add(5); // Element with key 5 is selected now
        assertEquals("map.getSelection().size()", 1, map.getSelectedKeys().size());
        assertTrue("map.getSelection().contains(5)", map.getSelectedKeys().contains(5));
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("map.getSelection().getMaxIndex()", 5, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 5, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysAddAll() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().addAll(map.keySet());
        assertEquals("map.getSelection().size()", NUM, map.getSelectedKeys().size());
        // we expect only single event since all indices build a single interval
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysSetAll_1() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().setAll(1, 2, 3);

        assertEquals("map.getSelection().size()", 3, map.getSelectedKeys().size());
        // we expect only single event since all indices build a single interval
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("map.getSelection().getMaxIndex()", 3, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 1, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysSetAll_2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().setAll(1, 2, 3, 7, 8, 9);

        assertEquals("map.getSelection().size()", 6, map.getSelectedKeys().size());
        // we expect 2 events since the indices build two distinct interval
        assertEquals("counter.elementsSelected", 2, counter.elementsSelected);
        assertEquals("map.getSelection().getMaxIndex()", 9, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 1, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysSetAll_3() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        map.getSelectedKeys().setAll(1, 2, 3);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().setAll(3, 4, 5);

        assertEquals("map.getSelection().size()", 3, map.getSelectedKeys().size());
        // we expect only single event since the indices build a single interval
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        // we expect only one single deselected event since the indices 1,2 build a single interval
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", 5, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 3, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysSetAll_4() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        map.getSelectedKeys().setAll(1, 2, 3);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().setAll(3, 7, 8);

        assertEquals("map.getSelection().size()", 3, map.getSelectedKeys().size());
        // we expect only one single selected event since the indices 7,8 build a single interval
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        // we expect only one single deselected event since the indices 1,2 build a single interval
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", 8, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 3, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysRemove() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        map.getSelectedKeys().add(5); // element with key 5 is selected now
        assertEquals("map.getSelection().getMaxIndex()", 5, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 5, map.getSelection().getMinIndex());

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().remove(5); // deselect element with key == 5
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysRemoveAll() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        map.getSelectedKeys().add(1);
        map.getSelectedKeys().add(5);
        map.getSelectedKeys().add(6);
        map.getSelectedKeys().add(7);
        map.getSelectedKeys().add(8);
        map.getSelectedKeys().add(9);
        // result selection: 1,5,6,7,8,9

        assertEquals("map.getSelection().getMaxIndex()", 9, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 1, map.getSelection().getMinIndex());

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        List<Integer> removeKeys = Arrays.asList(5, 6, 7, 9);

        map.getSelectedKeys().removeAll(removeKeys);
        // result selection: 1,8
        assertEquals("map.getSelection().size()", 2, map.getSelectedKeys().size());
        // we expect 2 events since the indices build two separate intervals
        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", 8, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 1, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysRetainAll() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        map.getSelectedKeys().add(1);
        map.getSelectedKeys().add(5);
        map.getSelectedKeys().add(6);
        map.getSelectedKeys().add(7);
        map.getSelectedKeys().add(8);
        map.getSelectedKeys().add(9);
        // result selection: 1,5,6,7,8,9
        assertEquals("map.getSelection().getMaxIndex()", 9, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 1, map.getSelection().getMinIndex());

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        List<Integer> retainKeys = Arrays.asList(5, 6, 7, 9);

        map.getSelectedKeys().retainAll(retainKeys);
        // result selection: 5,6,7,9
        // removed: 1,8
        assertEquals("map.getSelection().size()", 4, map.getSelectedKeys().size());
        // we expect 2 events since the indices build two separate intervals
        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", 9, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 5, map.getSelection().getMinIndex());
    }

    @Test
    public void putSingle() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        DummyPM newElem = new DummyPM();
        map.put(10, newElem); // the key 10 doesn't exist
        assertEquals("map.size()", NUM + 1, map.size());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void putWithIndex() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        DummyPM newElem = new DummyPM();
        map.put(10, newElem, 0);
        assertEquals("map.size()", NUM + 1, map.size());
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("map.getAt(0)", newElem, map.getAt(0));
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void putWithIndex2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        DummyPM newElem = new DummyPM();
        map.put(10, newElem, 10);
        assertEquals("map.size()", NUM + 1, map.size());
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("map.getAt(10)", newElem, map.getAt(10));
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void replaceWithIndex() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        DummyPM newElem = new DummyPM();
        map.put(5, newElem, 0);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("map.getAt(0)", newElem, map.getAt(0));
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void replaceWithIndex2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        DummyPM newElem = new DummyPM();
        map.put(5, newElem, 9);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("map.getAt(9)", newElem, map.getAt(9));
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void replaceWithIndex3() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        DummyPM newElem = new DummyPM();
        map.put(5, newElem, 5); // same index
        assertEquals("map.size()", NUM, map.size());
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 1, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("map.getAt(5)", newElem, map.getAt(5));
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void swapNeighbors() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.swap(0, 1);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("map.getAt(1)", map.get(0), map.getAt(1));
        assertEquals("map.getAt(0)", map.get(1), map.getAt(0));
        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 2, counter.elementsAdded);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void swapNeighbors2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.swap(5, 6);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("map.getAt(5)", map.get(6), map.getAt(5));
        assertEquals("map.getAt(6)", map.get(5), map.getAt(6));
        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 2, counter.elementsAdded);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void swap1() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.swap(4, 7);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("map.getAt(7)", map.get(4), map.getAt(7));
        assertEquals("map.getAt(4)", map.get(7), map.getAt(4));

        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsAdded", 2, counter.elementsAdded);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void replaceSingle() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        DummyPM newElem = new DummyPM();
        map.put(5, newElem); // the key 5 already exists
        assertEquals("map.size()", NUM, map.size());
        assertEquals("counter.elementReplaced", 1, counter.elementsReplaced);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void removeByIndex() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        // remove the first element
        map.removeAt(0);
        assertEquals("map.size()", NUM - 1, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void removeKey() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        // remove the element with key == 5
        map.removeKey(5);
        assertEquals("map.size()", NUM - 1, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void removeKey_CheckSelection() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelectedKeys().add(5);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        // remove the element with key == 5
        map.removeKey(5);
        assertEquals("map.size()", NUM - 1, map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("counter.elementsRemoved", 1, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void remove() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        // get the element with index == 5
        DummyPM elem5 = map.getAt(5);
        // remove it
        boolean result = map.remove(elem5);
        assertTrue("result", result);
        assertEquals("map.size()", NUM - 1, map.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void remove2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        // create a dummy pM that is not element of the map
        DummyPM dummy = new DummyPM();
        // try to remove it
        boolean result = map.remove(dummy);
        assertFalse("result", result);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void getFirst() {
        MapPM<String, DummyPM> map = new MapPM<String, DummyPM>();
        List<DummyPM> list = new ArrayList<DummyPM>();
        for (int i = 0; i < 10; ++i) {
            String key = "" + i;
            DummyPM model = new DummyPM(key);
            list.add(model);
            map.put(key, model);
        }
        assertEquals("map.size()", 10, map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelection().size());

        for (int i = 0; i < 10; ++i) {
            String key = "" + i;
            map.getSelectedKeys().add(key);
            assertEquals("map.getSelection().size()", i + 1, map.getSelection().size());
            assertEquals("map.getSelection().getFirst()", list.get(0), map.getSelection().getFirst());
            assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
            assertEquals("map.getSelection().getMaxIndex()", i, map.getSelection().getMaxIndex());
        }
    }

    @Test
    public void getFirst2() {
        MapPM<String, DummyPM> map = new MapPM<String, DummyPM>();

        map.put("3", new DummyPM("3"));
        map.put("2", new DummyPM("2"));
        map.put("1", new DummyPM("1"));
        DummyPM elem1 = map.get("1");
        DummyPM elem2 = map.get("2");
        DummyPM elem3 = map.get("3");

        map.getSelectedKeys().add("2");

        assertEquals("map.getSelection().getFirst()", elem2, map.getSelection().getFirst());
        assertEquals("map.getSelection().getMaxIndex()", 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 1, map.getSelection().getMinIndex());
        map.getSelectedKeys().add("1");
        assertEquals("map.getSelection().getFirst()", elem2, map.getSelection().getFirst());
        assertEquals("map.getSelection().getMaxIndex()", 2, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 1, map.getSelection().getMinIndex());
        map.getSelectedKeys().add("3");
        assertEquals("map.getSelection().getFirst()", elem3, map.getSelection().getFirst());
        assertEquals("map.getSelection().getMaxIndex()", 2, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
    }

    @Test
    public void iterator() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        Iterator<DummyPM> it = map.iterator();
        for (int i = 0; i < NUM; ++i) {
            assertTrue("it.hasNext()", it.hasNext());
            assertEquals("it.next()", map.getAt(i), it.next());
        }
        assertFalse("it.hasNext()", it.hasNext());
        try {
            it.next();
            fail("expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // ok.
        }
    }

    @Test
    public void keyiterator() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        Iterator<Integer> it = map.keyiterator();
        for (int i = 0; i < NUM; ++i) {
            assertTrue("it.hasNext()", it.hasNext());
            assertEquals("it.next()", map.getKey(i), it.next());
        }
        assertFalse("it.hasNext()", it.hasNext());
        try {
            it.next();
            fail("expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // ok.
        }
    }

    @Test
    public void listIterator() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        ListIterator<DummyPM> iter = map.listIterator(0);
        assertNotNull("iter", iter);
        for (int i = 0; i < map.size(); ++i) {
            assertTrue("iter.hasNext()", iter.hasNext());
            DummyPM model = iter.next();
            assertEquals("map", map.getAt(i), model);
        }
        assertFalse("iter.hasNext()", iter.hasNext());
    }

    @Test
    public void listIteratorV2() {
        int startIndex = 5;

        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        ListIterator<DummyPM> iter = map.listIterator(startIndex);
        assertNotNull("iter", iter);
        for (int i = startIndex; i < map.size(); ++i) {
            assertTrue("iter.hasNext()", iter.hasNext());
            DummyPM model = iter.next();
            assertEquals("map", map.getAt(i), model);
        }
        assertFalse("iter.hasNext()", iter.hasNext());
    }

    @Test
    public void listIteratorReverse() {
        int startIndex = 5;

        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);

        ListIterator<DummyPM> iter = map.listIterator(startIndex);
        assertNotNull("iter", iter);
        for (int i = startIndex - 1; i >= 0; --i) {
            assertTrue("iter.hasPrevious()", iter.hasPrevious());
            DummyPM model = iter.previous();
            assertEquals("map", map.getAt(i), model);
        }
        assertFalse("iter.hasPrevious()", iter.hasPrevious());
    }

    @Test
    public void removeAllKeys() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        HashSet<Integer> keySetToRemove = new HashSet<Integer>();
        keySetToRemove.add(3);
        keySetToRemove.add(4);
        keySetToRemove.add(5);
        keySetToRemove.add(8);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.removeAllKeys(keySetToRemove);

        assertEquals("map.size()", NUM - keySetToRemove.size(), map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void removeAllKeysWithSelection() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        HashSet<Integer> keySetToRemove = new HashSet<Integer>();
        keySetToRemove.add(3);
        keySetToRemove.add(4);
        keySetToRemove.add(5);
        keySetToRemove.add(8);

        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.removeAllKeys(keySetToRemove);

        assertEquals("map.size()", NUM - keySetToRemove.size(), map.size());
        assertEquals("map.getSelection().size()", NUM - keySetToRemove.size(), map.getSelectedKeys().size());
        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1 - 4, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
        for (int i = 0; i < map.size(); ++i) {
            assertTrue("map.getSelection().contains(i)", map.getSelection().contains(i));
        }

        List<ElementsDeselectedEvent> deselectionEvents = new LinkedList<ElementsDeselectedEvent>();
        for (ListEvent evt : counter.events) {
            if (evt instanceof ElementsDeselectedEvent) {
                deselectionEvents.add((ElementsDeselectedEvent)evt);
            }
        }
        assertEquals("deselectionEvents.size()", 2, deselectionEvents.size());
        // Note: deselection events are reverse ordered
        // since elements are removed from back to front
        assertEquals("deselectionEvents.get(1).getBeginIndex()", 8, deselectionEvents.get(0).getBeginIndex());
        assertEquals("deselectionEvents.get(1).getBeginIndex()", 1, deselectionEvents.get(0).getLength());
        assertEquals("deselectionEvents.get(0).getBeginIndex()", 3, deselectionEvents.get(1).getBeginIndex());
        assertEquals("deselectionEvents.get(0).getBeginIndex()", 3, deselectionEvents.get(1).getLength());
    }

    @Test
    public void removeAllIndices() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        int[] indexes = { 2, 3, 4, 7, 8, 9 };

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.removeAllIndices(indexes);

        assertEquals("map.size()", NUM - indexes.length, map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void removeAllIndicesShuffled() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        int[] indexes = { 4, 7, 2, 8, 3, 9 };

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.removeAllIndices(indexes);

        assertEquals("map.size()", NUM - indexes.length, map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void removeAllIndicesShuffledSelected() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        int[] indexes = { 4, 7, 2, 8, 3, 9 };
        map.getSelection().addAll();
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.removeAllIndices(indexes);

        assertEquals("map.size()", NUM - indexes.length, map.size());
        assertEquals("map.getSelection().size()", NUM - indexes.length, map.getSelectedKeys().size());
        assertEquals("counter.elementsRemoved", 2, counter.elementsRemoved);
        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1 - 6, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
        for (int i = 0; i < map.size(); ++i) {
            assertTrue("map.getSelection().contains(i)", map.getSelection().contains(i));
        }
    }

    @Test
    public void putAll() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        int MORE = 10;
        HashMap<Integer, DummyPM> hashmap = new HashMap<Integer, DummyPM>();
        for (int i = NUM; i < NUM + MORE; ++i) {
            hashmap.put(i, new DummyPM());
        }

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.putAll(hashmap);
        assertEquals("map.size()", NUM + MORE, map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementReplaced", 0, counter.elementsReplaced);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        for (int i = 0; i < NUM; ++i) {
            assertFalse("map.containsValue(pM.getAt(i))", hashmap.containsValue(map.getAt(i)));
        }
        for (int i = NUM; i < NUM + MORE; ++i) {
            assertTrue("map.containsValue(pM.getAt(i))", hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllOneReplacement() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        HashMap<Integer, DummyPM> hashmap = new HashMap<Integer, DummyPM>();
        for (int i = 0; i < 1; ++i) {
            hashmap.put(i, new DummyPM());
        }

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.putAll(hashmap);
        assertEquals("map.size()", NUM, map.size());
        assertTrue("contains(map.get(0)", map.contains(hashmap.get(0)));
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementReplaced", 1, counter.elementsReplaced);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        for (int i = 0; i < 1; ++i) {
            assertTrue("map.containsValue(pM.getAt(i='" + i + "'))", hashmap.containsValue(map.getAt(i)));
        }
        for (int i = 1; i < NUM; ++i) {
            assertFalse("map.containsValue(pM.getAt(i='" + i + "'))", hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllTotalReplacement() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        HashMap<Integer, DummyPM> hashmap = new HashMap<Integer, DummyPM>();
        for (int i = 0; i < NUM; ++i) {
            hashmap.put(i, new DummyPM());
        }

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.putAll(hashmap);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementReplaced", 1, counter.elementsReplaced);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        for (int i = 0; i < NUM; ++i) {
            assertTrue("map.containsValue(pM.getAt(i='" + i + "'))", hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllPartialReplacement() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        int MORE = 10;
        int START = 5;
        HashMap<Integer, DummyPM> hashmap = new HashMap<Integer, DummyPM>();
        for (int i = START; i < START + MORE; ++i) {
            hashmap.put(i, new DummyPM());
        }

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.putAll(hashmap);
        assertEquals("map.size()", NUM + MORE - START, map.size());
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementReplaced", 1, counter.elementsReplaced);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        for (int i = 0; i < START; ++i) {
            assertFalse("map.containsValue(pM.getAt(i))", hashmap.containsValue(map.getAt(i)));
        }
        for (int i = START; i < START + MORE; ++i) {
            assertTrue("map.containsValue(pM.getAt(i='" + i + "'))", hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllPartialReplacementWithSelection() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll(); // select all
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
        int MORE = 10;
        int START = 5;
        HashMap<Integer, DummyPM> hashmap = new HashMap<Integer, DummyPM>();
        for (int i = START; i < START + MORE; ++i) {
            hashmap.put(i, new DummyPM());
        }

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.putAll(hashmap);
        assertEquals("map.size()", NUM + MORE - START, map.size());
        assertEquals("map.getSelection().size()", NUM, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementReplaced", 1, counter.elementsReplaced);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        for (int i = 0; i < START; ++i) {
            assertFalse("map.containsValue(pM.getAt(i))", hashmap.containsValue(map.getAt(i)));
            assertTrue("map.getSelection().contains(i)", map.getSelection().contains(i));
        }
        for (int i = START; i < NUM; ++i) {
            assertTrue("map.containsValue(pM.getAt(i='" + i + "'))", hashmap.containsValue(map.getAt(i)));
            assertTrue("map.getSelection().contains(i)", map.getSelection().contains(i));
        }
        for (int i = NUM; i < MORE; ++i) {
            assertTrue("map.containsValue(pM.getAt(i='" + i + "'))", hashmap.containsValue(map.getAt(i)));
            assertFalse("map.getSelection().contains(i)", map.getSelection().contains(i));
        }
    }

    @Test
    public void selectionSetInterval() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelection().setInterval(0, 9);
        assertEquals("map.getSelection().size()", NUM, map.getSelection().size());
        assertEquals("map.getSelection().getMaxIndex()", 9, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());

        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
    }

    @Test
    public void selectionGetIndexes() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();

        int[] indexes = map.getSelection().getIndexes();
        assertEquals("indexes.length", NUM, indexes.length);
        for (int i = 0; i < indexes.length; ++i) {
            assertEquals("indexes[i='" + i + "']", i, indexes[i]);
        }
    }

    @Test
    public void selectionGetIndexes_v3() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addInterval(3, 7);

        int[] indexes = map.getSelection().getIndexes();
        assertEquals("indexes.length", 5, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);
        assertEquals("indexes[1]", 4, indexes[1]);
        assertEquals("indexes[2]", 5, indexes[2]);
        assertEquals("indexes[3]", 6, indexes[3]);
        assertEquals("indexes[4]", 7, indexes[4]);

        indexes = map.getSelection().getIndexes(0, 9);
        assertEquals("indexes.length", 5, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);
        assertEquals("indexes[1]", 4, indexes[1]);
        assertEquals("indexes[2]", 5, indexes[2]);
        assertEquals("indexes[3]", 6, indexes[3]);
        assertEquals("indexes[4]", 7, indexes[4]);

        indexes = map.getSelection().getIndexes(3, 7);
        assertEquals("indexes.length", 5, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);
        assertEquals("indexes[1]", 4, indexes[1]);
        assertEquals("indexes[2]", 5, indexes[2]);
        assertEquals("indexes[3]", 6, indexes[3]);
        assertEquals("indexes[4]", 7, indexes[4]);

        indexes = map.getSelection().getIndexes(4, 6);
        assertEquals("indexes.length", 3, indexes.length);
        assertEquals("indexes[0]", 4, indexes[0]);
        assertEquals("indexes[1]", 5, indexes[1]);
        assertEquals("indexes[2]", 6, indexes[2]);

        indexes = map.getSelection().getIndexes(0, 3);
        assertEquals("indexes.length", 1, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);

        indexes = map.getSelection().getIndexes(0, 2);
        assertEquals("indexes.length", 0, indexes.length);

        indexes = map.getSelection().getIndexes(8, 9);
        assertEquals("indexes.length", 0, indexes.length);
    }

    @Test
    public void selectionGetMinIndex() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();

        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
    }

    @Test
    public void selectionGetMaxIndex() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();

        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
    }

    @Test
    public void selectionSetInterval2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelection().setInterval(3, 5);
        assertEquals("map.getSelection().size()", 3, map.getSelection().size());
        assertEquals("map.getSelection().getMaxIndex()", 5, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 3, map.getSelection().getMinIndex());

        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
    }

    @Test
    public void selectionSetInterval3() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addInterval(0, 3);
        map.getSelection().addInterval(6, 9);
        assertEquals("map.getSelection().size()", 8, map.getSelection().size());
        assertEquals("map.getSelection().getMaxIndex()", 9, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelection().setInterval(2, 7);
        assertEquals("map.getSelection().size()", 6, map.getSelection().size());
        assertEquals("map.getSelection().getMaxIndex()", 7, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 2, map.getSelection().getMinIndex());

        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
    }

    @Test
    public void selectionGetMinIndexAndMaxIndex() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().setInterval(2, 3);
        assertEquals("map.getSelection().getMinIndex()", 2, map.getSelection().getMinIndex());
        assertEquals("map.getSelection().getMaxIndex()", 3, map.getSelection().getMaxIndex());

        map.getSelection().setInterval(5, 6);
        assertEquals("map.getSelection().getMinIndex()", 5, map.getSelection().getMinIndex());
        assertEquals("map.getSelection().getMaxIndex()", 6, map.getSelection().getMaxIndex());

        map.getSelection().addInterval(2, 3);
        assertEquals("map.getSelection().getMinIndex()", 2, map.getSelection().getMinIndex());
        assertEquals("map.getSelection().getMaxIndex()", 6, map.getSelection().getMaxIndex());

        map.getSelection().addInterval(9, 9);
        assertEquals("map.getSelection().getMinIndex()", 2, map.getSelection().getMinIndex());
        assertEquals("map.getSelection().getMaxIndex()", 9, map.getSelection().getMaxIndex());

        map.getSelection().removeInterval(6, 9);
        assertEquals("map.getSelection().getMinIndex()", 2, map.getSelection().getMinIndex());
        assertEquals("map.getSelection().getMaxIndex()", 5, map.getSelection().getMaxIndex());
    }

    public void selectionRemoveAll() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        assertEquals("map.getSelection().size()", 10, map.getSelection().size());
        Collection<DummyPM> selCopy = new ArrayList<DummyPM>(map.getSelection());
        map.getSelection().removeAll(selCopy);
        assertEquals("map.getSelection().size()", 0, map.getSelection().size());
    }

    public void selectionRemoveAll2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        EventCounter counter = new EventCounter();
        map.addListListener(counter);
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        assertEquals("map.getSelection().size()", 10, map.getSelection().size());
        Collection<DummyPM> col = new ArrayList<DummyPM>();
        col.add(new DummyPM());
        col.add(new DummyPM());
        map.getSelection().removeAll(col);
        assertEquals("map.getSelection().size()", 10, map.getSelection().size());
        assertEquals("map.getSelection().size()", 0, counter.elementsDeselected);

    }

    @Test
    public void removeElementRemovesSelection() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        assertEquals("map.getSelection().size()", 10, map.getSelection().size());

        map.remove(map.getAt(2));
        assertEquals("map.getSelection().size()", 9, map.getSelection().size());
    }

    @Test
    public void removeElementRemovesSelection2() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();

        DummyPM[] elems = populate(map, 5);

        map.getSelection().add(elems[2]);
        map.getSelection().add(elems[3]);
        assertEquals("map.getSelection().size()", 2, map.getSelection().size());

        map.remove(elems[2]);
        assertEquals("map.getSelection().size()", 1, map.getSelection().size());
    }

    @Test
    public void removeElementRemovesSelection3() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();

        DummyPM[] elems = populate(map, 10);

        map.getSelection().addAll();
        assertEquals("map.getSelection().size()", 10, map.getSelection().size());

        for (int i = 0; i < elems.length; ++i) {
            if (i % 2 == 0) {
                map.put(i, elems[i]);
            } else {
                map.remove(elems[i]);
            }
        }

        assertEquals("map.getSelection().size()", 5, map.getSelection().size());
    }

    @Test
    public void getSortKey() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        populate(map, 10);

        Collection<SortKey> sortKeys = map.getSortKeys();
        assertNotNull("sortKeys", sortKeys);
        assertEquals("sortKeys.size()", 0, sortKeys.size());

        map.sortBy(new SortKey(true, new Path("text")), new SortKey(true, new Path("id")));
        sortKeys = map.getSortKeys();

        assertNotNull("sortKeys", sortKeys);
        assertEquals("sortKeys.size()", 2, sortKeys.size());

        SortKey[] sortKeysArray = (SortKey[])sortKeys.toArray(new SortKey[sortKeys.size()]);
        assertEquals("sortKeysArray[0].getSortPath()", new Path("text"), sortKeysArray[0].getSortPath());
        assertEquals("sortKeysArray[1].getSortPath()", new Path("id"), sortKeysArray[1].getSortPath());

    }

    @Test
    public void multiplePuts() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        DummyPM[] elems = populate(map, 10);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.put(1, elems[1]);

        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementChanged", 0, counter.elementChanged);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
    }

    @Test
    public void multiplePutsWithSelection() {
        MapPM<Integer, DummyPM> map = new MapPM<Integer, DummyPM>();
        DummyPM[] elems = populate(map, 10);
        map.getSelection().addAll();

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.put(1, elems[1]);

        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementChanged", 0, counter.elementChanged);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
    }

    private DummyPM[] populate(MapPM<Integer, DummyPM> map, int number) {
        DummyPM[] elems = new DummyPM[number];
        for (int i = 0; i < elems.length; ++i) {
            elems[i] = new DummyPM();
            elems[i].id.setInteger(i);
            map.put(i, elems[i]);
        }
        assertEquals("map.size()", number, map.size());
        return elems;
    }

    private static class EventCounter implements ListListener {
        int elementChanged;
        int elementsReplaced;
        int elementsAdded;
        int elementsDeselected;
        int elementsRemoved;
        int elementsSelected;
        List<ListEvent> events = new LinkedList<ListEvent>();

        public void elementChanged(ElementChangedEvent evt) {
            events.add(evt);
            elementChanged++;
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            events.add(evt);
            elementsReplaced++;
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            events.add(evt);
            elementsAdded++;
        }

        public void elementsDeselected(ElementsDeselectedEvent evt) {
            events.add(evt);
            elementsDeselected++;
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            events.add(evt);
            elementsRemoved++;
        }

        public void elementsSelected(ElementsSelectedEvent evt) {
            events.add(evt);
            elementsSelected++;
        }
    }
}