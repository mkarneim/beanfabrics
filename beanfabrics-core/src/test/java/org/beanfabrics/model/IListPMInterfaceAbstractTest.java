/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.beanfabrics.Path;
import org.beanfabrics.event.ElementChangedEvent;
import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsDeselectedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.event.ElementsReplacedEvent;
import org.beanfabrics.event.ElementsSelectedEvent;
import org.beanfabrics.event.ListEvent;
import org.beanfabrics.event.ListListener;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public abstract class IListPMInterfaceAbstractTest {
    static int INITIAL_NUMBER_OF_ELEMENTS;
    static int[] INITIAL_SELECTED_ELEMENT_INDEXES;

    @BeforeClass
    public static void setUpClass()
        throws Exception {
        INITIAL_NUMBER_OF_ELEMENTS = 10;
        INITIAL_SELECTED_ELEMENT_INDEXES = new int[] { 0, 1, 2, 3, 4 };
    }

    public static class RowPM extends AbstractPM {
        IntegerPM id = new IntegerPM();
        TextPM name = new TextPM();
        IntegerPM section = new IntegerPM();

        public RowPM() {
            PMManager.setup(this);
        }
    }

    static class MyListListener implements ListListener {
        final List<ListEvent> events = new ArrayList<ListEvent>();

        /** {@inheritDoc} */
        public void elementChanged(ElementChangedEvent evt) {
            events.add(evt);
        }

        /** {@inheritDoc} */
        public void elementsAdded(ElementsAddedEvent evt) {
            events.add(evt);
        }

        /** {@inheritDoc} */
        public void elementsDeselected(ElementsDeselectedEvent evt) {
            events.add(evt);
        }

        /** {@inheritDoc} */
        public void elementsRemoved(ElementsRemovedEvent evt) {
            events.add(evt);
        }

        /** {@inheritDoc} */
        public void elementsReplaced(ElementsReplacedEvent evt) {
            events.add(evt);
        }

        /** {@inheritDoc} */
        public void elementsSelected(ElementsSelectedEvent evt) {
            events.add(evt);
        }

        @SuppressWarnings("unchecked")
        public <T extends ListEvent> List<T> getEventsOfType(Class<T> type) {
            List<T> result = new ArrayList<T>();
            for (ListEvent evt : events) {
                if (type.isInstance(evt)) {
                    result.add((T)evt);
                }
            }
            return result;
        }

        public void clear() {
            events.clear();
        }

    }

    MyListListener listListener;

    IListPM<RowPM> list;
    List<RowPM> elements;

    @Before
    public void setUp()
        throws Exception {
        Random rnd = new Random(3341);
        elements = new ArrayList<RowPM>(INITIAL_NUMBER_OF_ELEMENTS);
        for (int i = 0; i < INITIAL_NUMBER_OF_ELEMENTS; ++i) {
            RowPM row = new RowPM();
            row.id.setInteger(i);
            row.name.setText(createRandomText(rnd));
            row.section.setInteger(i % 2);
            elements.add(row);
        }
        list = create(elements, INITIAL_SELECTED_ELEMENT_INDEXES);
        listListener = new MyListListener();
        list.addListListener(listListener);
    }

    private String createRandomText(Random rnd) {
        final int len = 4;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < len; ++i) {
            double rndNum = rnd.nextDouble();
            int index = (int)(24.0d * rndNum) + 'a';
            buf.append(Character.toString((char)index));
        }
        return buf.toString();
    }

    protected abstract IListPM<RowPM> create(Collection<RowPM> elements, int[] selectedIndexes)
        throws Exception;

    @Test
    public void testSetUp() {
        assertNotNull("elements", elements);
        assertNotNull("list", list);
        assertEquals("elements.size()", INITIAL_NUMBER_OF_ELEMENTS, elements.size());
        assertEquals("list.size()", INITIAL_NUMBER_OF_ELEMENTS, list.size());
        RowPM[] elementsArray = elements.toArray(new RowPM[elements.size()]);
        RowPM[] listElementsArray = list.toCollection().toArray(new RowPM[elements.size()]);
        assertArrayEquals("elementsArray", elementsArray, listElementsArray);
        assertEquals("list.getSelection().size()", INITIAL_SELECTED_ELEMENT_INDEXES.length, list.getSelection().size());
        for (int index : INITIAL_SELECTED_ELEMENT_INDEXES) {
            assertEquals("list.getSelection().contains(index='" + index + "')", true, list.getSelection().contains(index));
        }
    }

    @Test
    public void size() {
        assertEquals("list.size()", INITIAL_NUMBER_OF_ELEMENTS, list.size());
    }

    @Test
    public void isEmpty() {
        assertEquals("list.isEmpty()", INITIAL_NUMBER_OF_ELEMENTS == 0, list.isEmpty());
    }

    @Test
    public void contains() {
        for (RowPM cell : elements) {
            assertEquals("list.contains(pM)", true, list.contains(cell));
        }
    }

    @Test
    public void indexOf() {
        for (int index = 0; index < elements.size(); ++index) {
            RowPM cell = elements.get(index);
            assertEquals("list.indexOf(pM)", index, list.indexOf(cell));
        }
    }

    @Test
    public void getAt() {
        for (int index = 0; index < elements.size(); ++index) {
            RowPM cell = elements.get(index);
            assertEquals("list.getAt(index='" + index + "')", cell, list.getAt(index));
        }
    }

    @Test
    public void toCollection() {
        Collection<RowPM> col = list.toCollection();
        assertNotNull("col", col);
        assertEquals("col.size()", elements.size(), col.size());
        Iterator<RowPM> it = col.iterator();
        for (int i = 0; i < elements.size(); ++i) {
            assertEquals("[i='" + i + "'] it.hasNext()", true, it.hasNext());
            RowPM element = it.next();
            assertSame("[i='" + i + "'] element", elements.get(i), element);
        }
    }

    @Test
    public void getSelection() {
        assertNotNull("list.getSelection()", list.getSelection());
        assertEquals("list.getSelection().size()", INITIAL_SELECTED_ELEMENT_INDEXES.length, list.getSelection().size());
    }

    @Test
    public void iterable() {
        Iterator<RowPM> it = list.iterator();
        assertNotNull("it", it);
        for (int i = 0; i < elements.size(); ++i) {
            assertEquals("[i='" + i + "'] it.hasNext()", true, it.hasNext());
            RowPM element = it.next();
            assertSame("[i='" + i + "'] element", elements.get(i), element);
        }
    }

    @Test
    public void sortIsStable() {
        list.sortBy(true, new Path("name"));
        list.sortBy(true, new Path("section"));

        Iterator<RowPM> it = list.iterator();
        RowPM curr = null;
        RowPM last = null;
        for (int i = 0; i < elements.size(); ++i) {
            curr = it.next();
            if (last != null && last.section.getText().equals(curr.section.getText())) {
                assertTrue("Element #" + i + " '" + curr.name.getText() + "' should be after element #" + (i - 1) + " '" + last.name.getText() + "'", curr.name.getText().compareTo(last.name.getText()) >= 0);
            }
            last = curr;
        }
    }

    @Test
    public void test1() {
        list.getSelection().addAll();
        assertEquals("list.getSelection().size()", elements.size(), list.getSelection().size());
        assertEquals("listListener.events.size()", 1, listListener.events.size());
        assertEquals("listListener.getEventsOfType(ElementsSelectedEvent.class).size()", 1, listListener.getEventsOfType(ElementsSelectedEvent.class).size());

        listListener.clear();

        List<RowPM> old = new ArrayList<RowPM>(elements);
        old.remove(0);
        list.getSelection().retainAll(old);

        assertEquals("list.getSelection().size()", old.size(), list.getSelection().size());
        assertEquals("listListener.getEventsOfType(ElementsSelectedEvent.class).size()", 1, listListener.getEventsOfType(ElementsDeselectedEvent.class).size());

    }

}