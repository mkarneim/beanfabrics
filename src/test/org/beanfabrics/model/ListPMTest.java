/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collection;
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
import org.beanfabrics.event.ListListener;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ListPMTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ListPMTest.class);
    }

    private static class DummyPM extends AbstractPM {
        public final IntegerPM id = new IntegerPM();
        public final TextPM text = new TextPM();
        public final SubDummyPM subDummy = new SubDummyPM();

        public DummyPM() {
            PMManager.setup(this);
        }

        public DummyPM(int id, String text, String subText) {
            this();
            this.id.setInteger(id);
            this.text.setText(text);
            this.subDummy.text.setText(subText);
        }

        public String getId() {
            return id.getText();
        }
    }

    private static class SubDummyPM extends AbstractPM {
        public final TextPM text = new TextPM();

        public SubDummyPM() {
            PMManager.setup(this);
        }
    }

    ListPM<DummyPM> list;
    int size;
    EventCounter counter;
    int elementIndex;
    DummyPM element;

    public ListPMTest() {
    }

    @Before
    public void setUp()
        throws Exception {
        list = new ListPM<DummyPM>();
        size = 10;
        elementIndex = 5;
        for (int i = 0; i < size; ++i) {
            DummyPM dummy = new DummyPM();
            if (i == elementIndex) {
                element = dummy;
            }
            dummy.id.setInteger(i);
            list.add(dummy);
        }
        counter = new EventCounter();
        list.addListListener(counter);
    }

    @AfterClass
    public static void tearDownClass()
        throws Exception {
    }

    @Test
    public void add() {
        DummyPM dummy = new DummyPM();
        dummy.id.setInteger(9999);
        int index = list.size();

        list.add(dummy);
        assertEquals("list.getAt(index)", dummy, list.getAt(index));
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void addAtIndex() {
        DummyPM dummy = new DummyPM();
        dummy.id.setInteger(9999);
        int index = elementIndex;

        list.add(index, dummy);
        assertEquals("list.getAt(index)", dummy, list.getAt(index));
        assertEquals("list.getAt(index)", element, list.getAt(index + 1));
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);

        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void addAtIndexWithSelection() {
        DummyPM dummy = new DummyPM();
        dummy.id.setInteger(9999);
        int index = elementIndex;
        list.getSelection().setInterval(index, index);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);

        assertEquals("list.getSelection().getMaxIndex()", index, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", index, list.getSelection().getMinIndex());

        list.add(index, dummy);
        assertEquals("list.getAt(index)", dummy, list.getAt(index));
        assertEquals("list.getAt(index)", element, list.getAt(index + 1));
        assertEquals("list.getSelection().contains(index)", false, list.getSelection().contains(index));
        assertEquals("list.getSelection().contains(index+1)", true, list.getSelection().contains(index + 1));
        assertEquals("list.getSelection().getMaxIndex()", index + 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", index + 1, list.getSelection().getMinIndex());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
    }

    @Test
    public void addAll() {
        // just add one row
        Collection<DummyPM> newCol = new LinkedList<DummyPM>();
        DummyPM pModel = new DummyPM();
        int newId = size;
        pModel.id.setInteger(newId);
        newCol.add(pModel);

        list.addAll(newCol);
        assertEquals("list.size()", size + 1, list.size());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("list.contains(pModel)", true, list.contains(pModel));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());

        assertEquals("newCol.size()", 1, newCol.size());
        assertEquals("list.containsAll( newCol)", true, list.containsAll(newCol));
    }

    @Test
    public void addAll2() {
        Collection<DummyPM> newCol = new LinkedList<DummyPM>();
        final int addNum = 20;
        int startIndex = this.size;
        for (int i = 0; i < addNum; ++i) {
            DummyPM dummy = new DummyPM();
            dummy.id.setInteger(i + startIndex);
            newCol.add(dummy);
        }

        list.addAll(newCol);
        assertEquals("list.size()", size + addNum, list.size());
        assertEquals("counter.elementsAdded", 1, counter.elementsAdded);
        assertEquals("counter.elementsRemoved", 0, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 0, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        assertEquals("list.containsAll( newCol)", true, list.containsAll(newCol));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void sort() {
        Object[] initialOrder = list.toArray();
        list.sortBy(true, new Path("id"));
        Object[] order1 = list.toArray();
        for (int i = 0; i < initialOrder.length; ++i) {
            assertSame("order1[i='" + i + "']", initialOrder[i], order1[i]);
        }
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMinIndex());
        list.sortBy(false, new Path("id"));
        Object[] order2 = list.toArray();
        for (int i = 0; i < initialOrder.length; ++i) {
            assertSame("order2[i='" + i + "']", initialOrder[initialOrder.length - i - 1], order2[i]);
        }
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void sort2() {
        ListPM<DummyPM> myList = new ListPM<DummyPM>();
        List<DummyPM> dummyList = new ArrayList<DummyPM>();
        int nextId = 0;
        dummyList.add(new DummyPM(nextId++, "pineapple", ""));
        dummyList.add(new DummyPM(nextId++, "lemon", ""));
        dummyList.add(new DummyPM(nextId++, "apple", ""));
        dummyList.add(new DummyPM(nextId++, "orange", ""));
        myList.addAll(dummyList);
        Object[] initialOrder = myList.toArray();
        for (int i = 0; i < initialOrder.length; ++i) {
            assertSame("initialOrder[i='" + i + "']", initialOrder[i], dummyList.get(i));
        }

        myList.sortBy(true, new Path("this.text"));
        assertEquals("myList.getAt(0).text.getText()", "apple", myList.getAt(0).text.getText());
        assertEquals("myList.getAt(1).text.getText()", "lemon", myList.getAt(1).text.getText());
        assertEquals("myList.getAt(2).text.getText()", "orange", myList.getAt(2).text.getText());
        assertEquals("myList.getAt(3).text.getText()", "pineapple", myList.getAt(3).text.getText());
        assertEquals("myList.getSelection().getMaxIndex()", -1, myList.getSelection().getMaxIndex());
        assertEquals("myList.getSelection().getMinIndex()", -1, myList.getSelection().getMinIndex());
    }

    @Test
    public void sort3() {
        ListPM<DummyPM> myList = new ListPM<DummyPM>();
        List<DummyPM> dummyList = new ArrayList<DummyPM>();
        int nextId = 0;
        dummyList.add(new DummyPM(nextId++, "", "pineapple"));
        dummyList.add(new DummyPM(nextId++, "", "lemon"));
        dummyList.add(new DummyPM(nextId++, "", "apple"));
        dummyList.add(new DummyPM(nextId++, "", "orange"));
        myList.addAll(dummyList);
        Object[] initialOrder = myList.toArray();
        for (int i = 0; i < initialOrder.length; ++i) {
            assertSame("initialOrder[i='" + i + "']", initialOrder[i], dummyList.get(i));
        }

        myList.sortBy(true, new Path("this.subDummy.text"));
        assertEquals("myList.getAt(0).subDummy.text.getText()", "apple", myList.getAt(0).subDummy.text.getText());
        assertEquals("myList.getAt(1).subDummy.text.getText()", "lemon", myList.getAt(1).subDummy.text.getText());
        assertEquals("myList.getAt(2).subDummy.text.getText()", "orange", myList.getAt(2).subDummy.text.getText());
        assertEquals("myList.getAt(3).subDummy.text.getText()", "pineapple", myList.getAt(3).subDummy.text.getText());
        assertEquals("myList.getSelection().getMaxIndex()", -1, myList.getSelection().getMaxIndex());
        assertEquals("myList.getSelection().getMinIndex()", -1, myList.getSelection().getMinIndex());
    }

    @Test
    public void swapNeighborsAscending() {
        int indexA = 3;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 4;
        DummyPM dummy2 = list.getAt(indexB);

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void swapNeighborsDescendig() {
        int indexA = 4;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 3;
        DummyPM dummy2 = list.getAt(indexB);

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void swapNeighborsAscendingWithSelection() {
        int indexA = 3;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 4; // must be greater than indexA (for correct assertions below)
        DummyPM dummy2 = list.getAt(indexB);

        list.getSelection().add(dummy1);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("list.getSelection().getMaxIndex()", indexA, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexA, list.getSelection().getMinIndex());

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().contains(edA)", true, list.getSelection().contains(dummy1));
        assertEquals("list.getSelection().contains(edB)", false, list.getSelection().contains(dummy2));
        assertEquals("list.getSelection().getMaxIndex()", indexB, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexB, list.getSelection().getMinIndex());
        assertEquals("counter.elementsSelected", 1, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 2, counter.elementsSelected);
    }

    @Test
    public void swapNeighborsDescendingWithSelection() {
        int indexA = 4;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 3; // must be less than indexA for correct selection assertions below
        DummyPM dummy2 = list.getAt(indexB);

        list.getSelection().add(dummy1);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("list.getSelection().getMaxIndex()", indexA, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexA, list.getSelection().getMinIndex());

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().contains(edA)", true, list.getSelection().contains(dummy1));
        assertEquals("list.getSelection().contains(edB)", false, list.getSelection().contains(dummy2));
        assertEquals("list.getSelection().getMaxIndex()", indexB, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexB, list.getSelection().getMinIndex());
        assertEquals("counter.elementsSelected", 1, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 2, counter.elementsSelected);
    }

    @Test
    public void swapAscending() {
        int indexA = 1;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 6;
        DummyPM dummy2 = list.getAt(indexB);

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void swapAscending2() {
        int indexA = 4;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 7;
        DummyPM dummy2 = list.getAt(indexB);

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void swapDescendig() {
        int indexA = 6;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 1;
        DummyPM dummy2 = list.getAt(indexB);

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void swapAscendingWithSelection() {
        int indexA = 1;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 6; // must be greater than indexA
        DummyPM dummy2 = list.getAt(indexB);

        list.getSelection().add(dummy1);
        assertEquals("list.getSelection().getMaxIndex()", indexA, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexA, list.getSelection().getMinIndex());

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().contains(edA)", true, list.getSelection().contains(dummy1));
        assertEquals("list.getSelection().contains(edB)", false, list.getSelection().contains(dummy2));
        assertEquals("list.getSelection().getMaxIndex()", indexB, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexB, list.getSelection().getMinIndex());
    }

    @Test
    public void swapDescendingWithSelection() {
        int indexA = 6;
        DummyPM dummy1 = list.getAt(indexA);
        int indexB = 1; // must be less than indexA
        DummyPM dummy2 = list.getAt(indexB);

        list.getSelection().add(dummy1);
        assertEquals("list.getSelection().getMaxIndex()", indexA, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexA, list.getSelection().getMinIndex());

        list.swap(indexA, indexB);
        assertEquals("list.getAt(indexA)", dummy2, list.getAt(indexA));
        assertEquals("list.getAt(indexB)", dummy1, list.getAt(indexB));
        assertEquals("list.getSelection().contains(edA)", true, list.getSelection().contains(dummy1));
        assertEquals("list.getSelection().contains(edB)", false, list.getSelection().contains(dummy2));
        assertEquals("list.getSelection().getMaxIndex()", indexB, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", indexB, list.getSelection().getMinIndex());
    }

    @Test
    public void remove() {
        int sizeBefore = list.size();
        DummyPM pModel = list.getAt(0);
        assertEquals("list.contains(pModel)", true, list.contains(pModel));
        list.remove(pModel);

        assertEquals("list.size()", sizeBefore - 1, list.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("list.contains(pModel)", false, list.contains(pModel));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void removeAt() {
        int sizeBefore = list.size();
        DummyPM pModel = list.getAt(4);
        assertEquals("list.contains(pModel)", true, list.contains(pModel));
        list.getSelection().add(pModel);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);
        list.removeAt(4);

        assertEquals("list.size()", sizeBefore - 1, list.size());
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.contains(pModel)", false, list.contains(pModel));
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void removeAll() {
        Collection<DummyPM> copy = list.toCollection();
        list.removeAll(copy);
        assertEquals("list.size()", 0, list.size());
        // just a single event is expected since this is a continuous interval
        assertEquals("counter.elementsRemoved", 1, counter.elementsRemoved);
    }

    @Test
    public void removeAll0() {
        int sizeBefore = list.size();
        list.getSelection().addInterval(3, 8);
        Collection<DummyPM> selected = list.getSelection();
        Collection<DummyPM> copy = new LinkedList<DummyPM>(selected);
        list.removeAll(selected);

        assertEquals("list.size()", sizeBefore - 6, list.size());
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.getSelection().size()", 0, list.getSelection().size());
        assertEquals("selected.size()", 0, selected.size());
        for (DummyPM elem : copy) {
            assertEquals("list.contains(pModel)", false, list.contains(elem));
        }
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void removeAll2() {
        int sizeBefore = list.size();
        list.getSelection().addInterval(1, 3);
        list.getSelection().addInterval(8, 9);
        assertEquals("list.getSelection().getMaxIndex()", 9, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 1, list.getSelection().getMinIndex());

        Collection<DummyPM> copy = new LinkedList<DummyPM>(list.getSelection());
        list.removeAll(list.getSelection());

        assertEquals("list.size()", sizeBefore - 5, list.size());
        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("list.getSelection().size()", 0, list.getSelection().size());
        for (DummyPM elem : copy) {
            assertEquals("list.contains(pModel)", false, list.contains(elem));
        }
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    public void removeAll3() {
        int sizeBefore = list.size();
        Collection<DummyPM> col = new ArrayList<DummyPM>();
        col.add(new DummyPM());
        col.add(new DummyPM());
        list.removeAll(col);

        assertEquals("list.size()", sizeBefore, list.size());
        assertEquals("counter.elementsDeselected", 0, counter.elementsRemoved);
    }

    @Test
    public void contains() {
        assertEquals("list.contains(element)", true, list.contains(element));
        assertEquals("list.contains( new DummyPM())", false, list.contains(new DummyPM()));
    }

    @Test
    public void containsAll() {
        Collection<DummyPM> col = list.toCollection();
        assertEquals("list.containsAll(col)", true, list.containsAll(col));
    }

    @Test
    public void containsAll2() {
        Collection<DummyPM> col = list.toCollection();
        col.remove(element);
        assertEquals("list.containsAll(col)", true, list.containsAll(col));
    }

    @Test
    public void containsAll3() {
        Collection<DummyPM> col = list.toCollection();
        col.add(new DummyPM());
        assertEquals("list.containsAll(col)", false, list.containsAll(col));
    }

    @Test
    public void getAt() {
        assertEquals("list.getAt(elementIndex)", element, list.getAt(elementIndex));
    }

    @Test
    public void toCollection() {
        Collection<DummyPM> col = list.toCollection();
        assertEquals("col.size()", size, col.size());
    }

    @Test
    public void listIterator() {
        ListIterator<DummyPM> iter = list.listIterator(0);
        assertNotNull("iter", iter);
        for (int i = 0; i < this.size; ++i) {
            assertEquals("iter.hasNext()", true, iter.hasNext());
            DummyPM elem = iter.next();
            assertEquals("elem", list.getAt(i), elem);
        }
        assertEquals("iter.hasNext()", false, iter.hasNext());
    }

    @Test
    public void listIteratorV2() {
        int startIndex = 5;
        ListIterator<DummyPM> iter = list.listIterator(startIndex);
        assertNotNull("iter", iter);
        for (int i = startIndex; i < this.size; ++i) {
            assertEquals("iter.hasNext()", true, iter.hasNext());
            DummyPM elem = iter.next();
            assertEquals("elem", list.getAt(i), elem);
        }
        assertEquals("iter.hasNext()", false, iter.hasNext());
    }

    @Test
    public void listIteratorReverse() {
        int startIndex = 5;
        ListIterator<DummyPM> iter = list.listIterator(startIndex);
        assertNotNull("iter", iter);
        for (int i = startIndex - 1; i >= 0; --i) {
            assertEquals("iter.hasPrevious()", true, iter.hasPrevious());
            DummyPM elem = iter.previous();
            assertEquals("elem", list.getAt(i), elem);
        }
        assertEquals("iter.hasPrevious()", false, iter.hasPrevious());
    }

    @Test
    public void getSelection() {
        Selection<DummyPM> sel = list.getSelection();
        assertNotNull("sel", sel);
        assertEquals("sel.size()", 0, sel.size());

        list.getSelection().add(element);
        assertEquals("sel.size()", 1, sel.size());
    }

    @Test
    public void indexOf() {
        assertEquals("list.indexOf(element)", elementIndex, list.indexOf(element));
    }

    @Test
    public void indicesOf() {
        Collection<DummyPM> col = new LinkedList<DummyPM>();
        col.add(element);
        int[] indices = list.indicesOf(col);
        assertEquals("indices.length", 1, indices.length);
        assertEquals("indices[0]", elementIndex, indices[0]);
    }

    @Test
    public void indicesOf2() {
        int sizeBefore = list.size();
        Collection<DummyPM> copy = list.toCollection();
        int[] indices = list.indicesOf(copy);
        assertEquals("indices.length", sizeBefore, indices.length);

    }

    @Test
    public void isEmpty() {
        assertEquals("list.isEmpty()", false, list.isEmpty());
        list.clear();
        assertEquals("list.isEmpty()", true, list.isEmpty());
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void size() {
        assertEquals("list.size()", size, list.size());
    }

    @Test
    public void addAndRemoveSome() {
        for (int i = size; i < size + 15; ++i) {
            DummyPM elem = new DummyPM();
            elem.id.setInteger(i);
            list.add(elem);
        }
        assertEquals("list.size()", size + 15, list.size());
        assertEquals("counter.elementsAdded", 15, counter.elementsAdded);

        for (int i = size + 15 - 1; i >= size; --i) {
            list.removeAt(i);
        }
        assertEquals("list.size()", size, list.size());
        assertEquals("counter.elementsRemoved", 15, counter.elementsRemoved);
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionAddAll() {
        list.getSelection().addAll();
        assertEquals("list.getSelection().size()", size, list.getSelection().size());
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionClear() {
        list.getSelection().addAll();
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        list.getSelection().clear();
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.getSelection().size()", 0, list.getSelection().size());
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionAddInterval() {
        list.getSelection().addInterval(5, 9);
        assertEquals("list.getSelection().size()", 5, list.getSelection().size());
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
        for (int i = 5; i <= 9; ++i) {
            assertEquals("list.getSelection().contains(i)", true, list.getSelection().contains(i));
        }
        assertEquals("list.getSelection().getMaxIndex()", 9, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 5, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionContansIndex() {
        list.getSelection().add(element);
        assertEquals("list.getSelection().contains(elementIndex)", true, list.getSelection().contains(elementIndex));
    }

    @Test
    public void selectionContansObject() {
        list.getSelection().add(element);
        assertEquals("list.getSelection().contains(element)", true, list.getSelection().contains(element));
    }

    @Test
    public void selectionContansAll() {
        list.getSelection().addAll();
        assertEquals("list.getSelection().containsAll(list.getElements())", true, list.getSelection().containsAll(list.toCollection()));

        Collection<DummyPM> col = new LinkedList<DummyPM>();
        col.add(new DummyPM());
        assertEquals("list.getSelection().containsAll(col)", false, list.getSelection().containsAll(col));
    }

    @Test
    public void selectionGetIndexes() {
        list.getSelection().addAll();
        int[] indexes = list.getSelection().getIndexes();
        assertEquals("indexes.length", size, indexes.length);
        for (int i = 0; i < indexes.length; ++i) {
            assertEquals("indexes[i='" + i + "']", i, indexes[i]);
        }
    }

    @Test
    public void selectionGetIndexes_v2() {
        list.getSelection().addAll();
        list.getSelection().removeInterval(elementIndex, elementIndex);
        int[] indexes = list.getSelection().getIndexes();
        assertEquals("indexes.length", size - 1, indexes.length);
        for (int i = 0; i < elementIndex; ++i) {
            assertEquals("indexes[i='" + i + "']", i, indexes[i]);
        }
        for (int i = elementIndex; i < indexes.length; ++i) {
            assertEquals("indexes[i='" + i + "']", i + 1, indexes[i]);
        }
    }

    @Test
    public void selectionGetIndexes_v3() {
        list.getSelection().addInterval(3, 7);
        int[] indexes = list.getSelection().getIndexes();
        assertEquals("indexes.length", 5, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);
        assertEquals("indexes[1]", 4, indexes[1]);
        assertEquals("indexes[2]", 5, indexes[2]);
        assertEquals("indexes[3]", 6, indexes[3]);
        assertEquals("indexes[4]", 7, indexes[4]);

        indexes = list.getSelection().getIndexes(0, 9);
        assertEquals("indexes.length", 5, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);
        assertEquals("indexes[1]", 4, indexes[1]);
        assertEquals("indexes[2]", 5, indexes[2]);
        assertEquals("indexes[3]", 6, indexes[3]);
        assertEquals("indexes[4]", 7, indexes[4]);

        indexes = list.getSelection().getIndexes(3, 7);
        assertEquals("indexes.length", 5, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);
        assertEquals("indexes[1]", 4, indexes[1]);
        assertEquals("indexes[2]", 5, indexes[2]);
        assertEquals("indexes[3]", 6, indexes[3]);
        assertEquals("indexes[4]", 7, indexes[4]);

        indexes = list.getSelection().getIndexes(4, 6);
        assertEquals("indexes.length", 3, indexes.length);
        assertEquals("indexes[0]", 4, indexes[0]);
        assertEquals("indexes[1]", 5, indexes[1]);
        assertEquals("indexes[2]", 6, indexes[2]);

        indexes = list.getSelection().getIndexes(0, 3);
        assertEquals("indexes.length", 1, indexes.length);
        assertEquals("indexes[0]", 3, indexes[0]);

        indexes = list.getSelection().getIndexes(0, 2);
        assertEquals("indexes.length", 0, indexes.length);

        indexes = list.getSelection().getIndexes(8, 9);
        assertEquals("indexes.length", 0, indexes.length);
    }

    @Test
    public void selectionGetFirst() {
        list.getSelection().add(element);
        assertEquals("list.getSelection().getFirst()", element, list.getSelection().getFirst());
        list.getSelection().addInterval(1, 1);
        assertEquals("list.getSelection().getFirst()", list.getAt(1), list.getSelection().getFirst());
        list.getSelection().addInterval(9, 9);
        assertEquals("list.getSelection().getFirst()", list.getAt(1), list.getSelection().getFirst());
    }

    @Test
    public void selectionGetMinIndex() {
        list.getSelection().add(element);
        assertEquals("list.getSelection().getMinIndex()", elementIndex, list.getSelection().getMinIndex());
        list.getSelection().clear();
        list.getSelection().addInterval(1, 1);
        assertEquals("list.getSelection().getMinIndex()", 1, list.getSelection().getMinIndex());
        list.getSelection().addInterval(9, 9);
        assertEquals("list.getSelection().getMinIndex()", 1, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionGetMinIndexAndMaxIndex() {
        list.getSelection().setInterval(2, 3);
        assertEquals("list.getSelection().getMinIndex()", 2, list.getSelection().getMinIndex());
        assertEquals("list.getSelection().getMaxIndex()", 3, list.getSelection().getMaxIndex());

        list.getSelection().setInterval(5, 6);
        assertEquals("list.getSelection().getMinIndex()", 5, list.getSelection().getMinIndex());
        assertEquals("list.getSelection().getMaxIndex()", 6, list.getSelection().getMaxIndex());

        list.getSelection().addInterval(2, 3);
        assertEquals("list.getSelection().getMinIndex()", 2, list.getSelection().getMinIndex());
        assertEquals("list.getSelection().getMaxIndex()", 6, list.getSelection().getMaxIndex());

        list.getSelection().addInterval(9, 9);
        assertEquals("list.getSelection().getMinIndex()", 2, list.getSelection().getMinIndex());
        assertEquals("list.getSelection().getMaxIndex()", 9, list.getSelection().getMaxIndex());

        list.getSelection().removeInterval(6, 9);
        assertEquals("list.getSelection().getMinIndex()", 2, list.getSelection().getMinIndex());
        assertEquals("list.getSelection().getMaxIndex()", 5, list.getSelection().getMaxIndex());
    }

    @Test
    public void selectionGetMaxIndex() {
        list.getSelection().add(element);
        assertEquals("list.getSelection().getMaxIndex()", elementIndex, list.getSelection().getMaxIndex());
        list.getSelection().clear();
        list.getSelection().addInterval(1, 1);
        assertEquals("list.getSelection().getMaxIndex()", 1, list.getSelection().getMaxIndex());
        list.getSelection().addInterval(9, 9);
        assertEquals("list.getSelection().getMaxIndex()", 9, list.getSelection().getMaxIndex());
    }

    @Test
    public void selectionIsEmpty() {
        assertEquals("list.getSelection().isEmpty()", true, list.getSelection().isEmpty());
        list.getSelection().addInterval(1, 1);
        assertEquals("list.getSelection().isEmpty()", false, list.getSelection().isEmpty());
    }

    @Test
    public void selectionIterator() {
        list.getSelection().addInterval(1, 1);
        Iterator<DummyPM> it = list.getSelection().iterator();
        DummyPM firstElem = it.next();
        assertEquals("it.next()", list.getAt(1), firstElem);
    }

    @Test
    public void selectionIterator2() {
        list.getSelection().addInterval(1, 4);
        for (int i = 1; i <= 4; ++i) {
            assertEquals("list.getSelection().contains(i)", true, list.getSelection().contains(i));
        }
        Iterator<DummyPM> it = list.getSelection().iterator();
        int i = 1;
        while (it.hasNext()) {
            assertEquals("list.getSelection().contains(i='" + i + "')", true, list.getSelection().contains(i));
            assertEquals("i='" + i + "', it.next()", list.getAt(i), it.next());
            i++;
        }
    }

    @Test
    public void selectionIteratorRemove() {
        list.getSelection().addInterval(1, 4);
        Iterator<DummyPM> it = list.getSelection().iterator();
        int i = 1;
        while (it.hasNext()) {
            assertEquals("list.getSelection().contains(i)", true, list.getSelection().contains(i));
            assertEquals("list.getSelection().getMaxIndex()", 4, list.getSelection().getMaxIndex());
            assertEquals("list.getSelection().getMinIndex()", i, list.getSelection().getMinIndex());
            assertEquals("i='" + i + "', it.next()", list.getAt(i), it.next());
            it.remove();
            assertEquals("list.getSelection().contains(i)", false, list.getSelection().contains(i));
            i++;
        }
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionRemove() {
        list.getSelection().addAll();
        list.getSelection().remove(element);
        assertEquals("list.getSelection().contains(elementIndex)", false, list.getSelection().contains(elementIndex));
        assertEquals("list.getSelection().size()", size - 1, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionRemoveAll() {
        list.getSelection().addAll();
        list.getSelection().removeAll(list.toCollection());
        assertEquals("list.getSelection().size()", 0, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());

        list.getSelection().addAll();
        list.getSelection().remove(list.getAt(5));
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionRemoveAll2() {
        list.getSelection().addAll();

        list.getSelection().remove(list.getAt(5));
        assertEquals("list.getSelection().size()", size - 1, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());

        list.getSelection().removeAll(list.toCollection());
        assertEquals("list.getSelection().size()", 0, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 3, counter.elementsDeselected);
        assertEquals("list.getSelection().getMaxIndex()", -1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", -1, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionRemoveAll3() {
        list.getSelection().addAll();
        int selSize = list.getSelection().size();
        List<DummyPM> col = new ArrayList<DummyPM>();
        col.add(new DummyPM());
        col.add(new DummyPM());
        col.add(new DummyPM());
        list.getSelection().removeAll(col);
        assertEquals("list.getSelection().size()", selSize, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 0, counter.elementsDeselected);

    }

    @Test
    public void selectionRemoveInterval() {
        list.getSelection().addAll();
        list.getSelection().remove(list.getAt(5));
        assertEquals("list.getSelection().size()", size - 1, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());

        list.getSelection().removeInterval(3, 6);
        assertEquals("list.getSelection().size()", 6, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 3, counter.elementsDeselected);
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionRetainAll() {
        list.getSelection().addAll();
        Collection<DummyPM> col = list.toCollection();
        col.remove(list.getAt(elementIndex));
        list.getSelection().retainAll(col);
        assertEquals("list.getSelection().size()", 9, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 1, counter.elementsDeselected);
        assertEquals("list.getSelection().contains(element)", false, list.getSelection().contains(element));
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionRetainAll2() {
        list.getSelection().addAll();
        Collection<DummyPM> col = list.toCollection();
        col.remove(list.getAt(2));
        col.remove(list.getAt(6));
        list.getSelection().retainAll(col);
        assertEquals("list.getSelection().size()", 8, list.getSelection().size());
        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("list.getSelection().getMaxIndex()", size - 1, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
    }

    @Test
    public void selectionSize() {
        assertEquals("list.getSelection().size()", 0, list.getSelection().size());
    }

    @Test
    public void selectionToArray() {
        Object[] array = list.getSelection().toArray();
        assertEquals("array.length", 0, array.length);
    }

    @Test
    public void selectionToArray2() {
        list.getSelection().addInterval(2, 4);
        Object[] array = list.getSelection().toArray();
        assertEquals("array.length", 3, array.length);
        int arrayIndex = 0;
        for (int i = 2; i <= 4; ++i) {
            assertEquals("array[arrayIndex]", list.getAt(i), array[arrayIndex]);
            arrayIndex++;
        }
    }

    @Test
    public void selectionToGenericArray() {
        DummyPM[] array = list.getSelection().toArray(new DummyPM[0]);
        assertEquals("array.length", 0, array.length);
    }

    @Test
    public void selectionToGenericArray2() {
        list.getSelection().addInterval(2, 4);
        DummyPM[] array = list.getSelection().toArray(new DummyPM[0]);
        assertEquals("array.length", 3, array.length);
        int arrayIndex = 0;
        for (int i = 2; i <= 4; ++i) {
            assertEquals("array[arrayIndex]", list.getAt(i), array[arrayIndex]);
            arrayIndex++;
        }
    }

    @Test
    public void selectionSetInterval() {
        list.getSelection().setInterval(0, 9);
        assertEquals("pM.getSelection().size()", 10, list.getSelection().size());
        assertEquals("list.getSelection().getMaxIndex()", 9, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
    }

    @Test
    public void selectionSetInterval2() {
        list.getSelection().addAll();
        counter.reset();

        list.getSelection().setInterval(3, 5);
        assertEquals("pM.getSelection().size()", 3, list.getSelection().size());
        assertEquals("list.getSelection().getMaxIndex()", 5, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 3, list.getSelection().getMinIndex());

        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
    }

    @Test
    public void selectionSetInterval3() {
        list.getSelection().addInterval(0, 3);
        list.getSelection().addInterval(6, 9);
        assertEquals("pM.getSelection().size()", 8, list.getSelection().size());
        assertEquals("list.getSelection().getMaxIndex()", 9, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 0, list.getSelection().getMinIndex());
        counter.reset();

        list.getSelection().setInterval(2, 7);
        assertEquals("pM.getSelection().size()", 6, list.getSelection().size());
        assertEquals("list.getSelection().getMaxIndex()", 7, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 2, list.getSelection().getMinIndex());

        assertEquals("counter.elementsDeselected", 2, counter.elementsDeselected);
        assertEquals("counter.elementsSelected", 1, counter.elementsSelected);
    }

    @Test
    public void getMaxIndex1() {
        list.getSelection().addInterval(5, 6);
        assertEquals("list.getSelection().getMaxIndex()", 6, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 5, list.getSelection().getMinIndex());
        list.swap(4, 5);
        assertEquals("list.getSelection().getMaxIndex()", 6, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 4, list.getSelection().getMinIndex());
    }

    @Test
    public void getMaxIndex2() {
        list.getSelection().addInterval(5, 6);
        assertEquals("list.getSelection().getMaxIndex()", 6, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 5, list.getSelection().getMinIndex());
        list.swap(6, 7);
        assertEquals("list.getSelection().getMaxIndex()", 7, list.getSelection().getMaxIndex());
        assertEquals("list.getSelection().getMinIndex()", 5, list.getSelection().getMinIndex());

    }

    private static class EventCounter implements ListListener {
        int elementChanged;
        int elementReplaced;
        int elementsAdded;
        int elementsDeselected;
        int elementsRemoved;
        int elementsSelected;

        public void elementChanged(ElementChangedEvent evt) {
            elementChanged++;
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            elementReplaced++;
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            elementsAdded++;
        }

        public void elementsDeselected(ElementsDeselectedEvent evt) {
            elementsDeselected++;
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            elementsRemoved++;
        }

        public void elementsSelected(ElementsSelectedEvent evt) {
            elementsSelected++;
        }

        public void reset() {
            elementChanged = 0;
            elementReplaced = 0;
            elementsAdded = 0;
            elementsDeselected = 0;
            elementsRemoved = 0;
            elementsSelected = 0;
        }

    }
}