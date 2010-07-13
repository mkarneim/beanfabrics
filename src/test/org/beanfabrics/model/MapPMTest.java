/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;
import org.beanfabrics.swing.table.BnTableModel;
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
        new MapPM();
    }

    @Test
    public void putMany() {
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.getSelectedKeys().add(5); // Element with key 5 is selected now
        assertEquals("map.getSelection().size()", 1, map.getSelectedKeys().size());
        assertEquals("map.getSelection().contains(5)", true, map.getSelectedKeys().contains(5));
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("map.getSelection().getMaxIndex()", 5, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 5, map.getSelection().getMinIndex());
    }

    @Test
    public void selectedKeysAddAll() {
        MapPM map = new MapPM();
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
    public void selectedKeysRemove() {
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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

        List removeKeys = Arrays.asList(5, 6, 7, 9);

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
        MapPM map = new MapPM();
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

        List retainKeys = Arrays.asList(5, 6, 7, 9);

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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        TextPM newElem = new TextPM();

        map.put(10, newElem); // the key 10 doesn't exist
        assertEquals("map.size()", NUM + 1, map.size());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void putWithIndex() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        TextPM newElem = new TextPM();

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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        TextPM newElem = new TextPM();

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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        TextPM newElem = new TextPM();

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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        TextPM newElem = new TextPM();

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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        TextPM newElem = new TextPM();

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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        TextPM newElem = new TextPM();

        map.put(5, newElem); // the key 5 already exists
        assertEquals("map.size()", NUM, map.size());
        assertEquals("counter.elementReplaced", 1, counter.elementsReplaced);
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
    }

    @Test
    public void removeByIndex() {
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        assertEquals("result", true, result);
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
        assertEquals("result", false, result);
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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        Iterator it = map.iterator();
        for (int i = 0; i < NUM; ++i) {
            assertEquals("it.hasNext()", true, it.hasNext());
            assertEquals("it.next()", map.getAt(i), it.next());
        }
        assertEquals("it.hasNext()", false, it.hasNext());
        try {
            it.next();
            fail("expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // ok.
        }
    }

    @Test
    public void keyiterator() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        Iterator it = map.keyiterator();
        for (int i = 0; i < NUM; ++i) {
            assertEquals("it.hasNext()", true, it.hasNext());
            assertEquals("it.next()", map.getKey(i), it.next());
        }
        assertEquals("it.hasNext()", false, it.hasNext());
        try {
            it.next();
            fail("expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // ok.
        }
    }

    @Test
    public void listIterator() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        ListIterator<DummyPM> iter = map.listIterator(0);
        assertNotNull("iter", iter);
        for (int i = 0; i < map.size(); ++i) {
            assertEquals("iter.hasNext()", true, iter.hasNext());
            DummyPM model = iter.next();
            assertEquals("map", map.getAt(i), model);
        }
        assertEquals("iter.hasNext()", false, iter.hasNext());
    }

    @Test
    public void listIteratorV2() {
        int startIndex = 5;

        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        ListIterator<DummyPM> iter = map.listIterator(startIndex);
        assertNotNull("iter", iter);
        for (int i = startIndex; i < map.size(); ++i) {
            assertEquals("iter.hasNext()", true, iter.hasNext());
            DummyPM model = iter.next();
            assertEquals("map", map.getAt(i), model);
        }
        assertEquals("iter.hasNext()", false, iter.hasNext());
    }

    @Test
    public void listIteratorReverse() {
        int startIndex = 5;

        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);

        ListIterator<DummyPM> iter = map.listIterator(startIndex);
        assertNotNull("iter", iter);
        for (int i = startIndex - 1; i >= 0; --i) {
            assertEquals("iter.hasPrevious()", true, iter.hasPrevious());
            DummyPM model = iter.previous();
            assertEquals("map", map.getAt(i), model);
        }
        assertEquals("iter.hasPrevious()", false, iter.hasPrevious());
    }

    @Test
    public void removeAllKeys() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        HashSet keySetToRemove = new HashSet();
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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        HashSet keySetToRemove = new HashSet();
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
            assertEquals("map.getSelection().contains(i)", true, map.getSelection().contains(i));
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
            assertEquals("map.getSelection().contains(i)", true, map.getSelection().contains(i));
        }
    }

    @Test
    public void putAll() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        int MORE = 10;
        HashMap<Integer, PresentationModel> hashmap = new HashMap<Integer, PresentationModel>();
        for (int i = NUM; i < NUM + MORE; ++i) {
            hashmap.put(i, new TextPM());
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
            assertEquals("map.containsValue(pM.getAt(i))", false, hashmap.containsValue(map.getAt(i)));
        }
        for (int i = NUM; i < NUM + MORE; ++i) {
            assertEquals("map.containsValue(pM.getAt(i))", true, hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllOneReplacement() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        HashMap<Integer, PresentationModel> hashmap = new HashMap<Integer, PresentationModel>();
        for (int i = 0; i < 1; ++i) {
            hashmap.put(i, new TextPM());
        }

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        map.putAll(hashmap);
        assertEquals("map.size()", NUM, map.size());
        assertEquals("contains(map.get(0)", true, map.contains(hashmap.get(0)));
        assertEquals("map.getSelection().size()", 0, map.getSelectedKeys().size());
        assertEquals("map.getSelection().getMaxIndex()", -1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", -1, map.getSelection().getMinIndex());
        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementReplaced", 1, counter.elementsReplaced);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        for (int i = 0; i < 1; ++i) {
            assertEquals("map.containsValue(pM.getAt(i='" + i + "'))", true, hashmap.containsValue(map.getAt(i)));
        }
        for (int i = 1; i < NUM; ++i) {
            assertEquals("map.containsValue(pM.getAt(i='" + i + "'))", false, hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllTotalReplacement() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        HashMap<Integer, PresentationModel> hashmap = new HashMap<Integer, PresentationModel>();
        for (int i = 0; i < NUM; ++i) {
            hashmap.put(i, new TextPM());
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
            assertEquals("map.containsValue(pM.getAt(i='" + i + "'))", true, hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllPartialReplacement() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        int MORE = 10;
        int START = 5;
        HashMap<Integer, PresentationModel> hashmap = new HashMap<Integer, PresentationModel>();
        for (int i = START; i < START + MORE; ++i) {
            hashmap.put(i, new TextPM());
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
            assertEquals("map.containsValue(pM.getAt(i))", false, hashmap.containsValue(map.getAt(i)));
        }
        for (int i = START; i < START + MORE; ++i) {
            assertEquals("map.containsValue(pM.getAt(i='" + i + "'))", true, hashmap.containsValue(map.getAt(i)));
        }
    }

    @Test
    public void putAllPartialReplacementWithSelection() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll(); // select all
        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
        int MORE = 10;
        int START = 5;
        HashMap<Integer, PresentationModel> hashmap = new HashMap<Integer, PresentationModel>();
        for (int i = START; i < START + MORE; ++i) {
            hashmap.put(i, new TextPM());
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
            assertEquals("map.containsValue(pM.getAt(i))", false, hashmap.containsValue(map.getAt(i)));
            assertEquals("map.getSelection().contains(i)", true, map.getSelection().contains(i));
        }
        for (int i = START; i < NUM; ++i) {
            assertEquals("map.containsValue(pM.getAt(i='" + i + "'))", true, hashmap.containsValue(map.getAt(i)));
            assertEquals("map.getSelection().contains(i)", true, map.getSelection().contains(i));
        }
        for (int i = NUM; i < MORE; ++i) {
            assertEquals("map.containsValue(pM.getAt(i='" + i + "'))", true, hashmap.containsValue(map.getAt(i)));
            assertEquals("map.getSelection().contains(i)", false, map.getSelection().contains(i));
        }
    }

    @Test
    public void selectionSetInterval() {
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();

        assertEquals("map.getSelection().getMinIndex()", 0, map.getSelection().getMinIndex());
    }

    @Test
    public void selectionGetMaxIndex() {
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();

        assertEquals("map.getSelection().getMaxIndex()", NUM - 1, map.getSelection().getMaxIndex());
    }

    @Test
    public void selectionSetInterval2() {
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        assertEquals("map.getSelection().size()", 10, map.getSelection().size());
        Collection<DummyPM> selCopy = new ArrayList<DummyPM>(map.getSelection());
        map.getSelection().removeAll(selCopy);
        assertEquals("map.getSelection().size()", 0, map.getSelection().size());
    }

    public void selectionRemoveAll2() {
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
        final int NUM = 10;
        populate(map, NUM);
        map.getSelection().addAll();
        assertEquals("map.getSelection().size()", 10, map.getSelection().size());

        map.remove(map.getAt(2));
        assertEquals("map.getSelection().size()", 9, map.getSelection().size());
    }

    @Test
    public void removeElementRemovesSelection2() {
        MapPM map = new MapPM();

        DummyPM[] elems = populate(map, 5);

        map.getSelection().add(elems[2]);
        map.getSelection().add(elems[3]);
        assertEquals("map.getSelection().size()", 2, map.getSelection().size());

        map.remove(elems[2]);
        assertEquals("map.getSelection().size()", 1, map.getSelection().size());
    }

    @Test
    public void removeElementRemovesSelection3() {
        MapPM map = new MapPM();

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
        MapPM map = new MapPM();
        DummyPM[] elems = populate(map, 10);

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
        MapPM map = new MapPM();
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
        MapPM map = new MapPM();
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

    @Test
    public void multiplePutsWithSelectionAndBnTableModel() {
        MapPM map = new MapPM();
        DummyPM[] elems = populate(map, 10);
        map.getSelection().addAll();

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        List<BnColumn> cols = new ArrayList<BnColumn>();
        cols.add(new BnColumn(new Path("id"), "ID"));
        BnTableModel model = new BnTableModel(map, cols, true);

        map.put(1, elems[1]);

        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementChanged", 0, counter.elementChanged);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
    }

    @Test
    public void multiplePutsWithSelectionAndBnTable() {
        MapPM map = new MapPM();
        DummyPM[] elems = populate(map, 10);
        map.getSelection().addAll();

        EventCounter counter = new EventCounter();
        map.addListListener(counter);

        List<BnColumn> cols = new ArrayList<BnColumn>();
        cols.add(new BnColumn(new Path("id"), "ID"));
        BnTable table = new BnTable();
        table.setPresentationModel(map);
        table.setColumns(cols.toArray(new BnColumn[cols.size()]));

        map.put(1, elems[1]);

        assertEquals("counter.elementsAdded", 0, counter.elementsAdded);
        assertEquals("counter.elementChanged", 0, counter.elementChanged);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
    }

    private DummyPM[] populate(MapPM map, int number) {
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