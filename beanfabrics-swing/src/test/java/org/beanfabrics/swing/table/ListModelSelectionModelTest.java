/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class ListModelSelectionModelTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ListModelSelectionModelTest.class);
    }

    private int NUM = 10;
    private MapPM<Integer, MyModel> mapProperty;
    private List<BnColumn> def;

    @Before
    public void setUp()
        throws Exception {
        this.mapProperty = this.createMapProperty();
        this.def = this.createTableColumnDefinition();
    }

    private List<BnColumn> createTableColumnDefinition() {
        final LinkedList<BnColumn> list = new LinkedList<BnColumn>();
        list.add(new BnColumn(new Path("text"), "Text"));
        return list;
    }

    private MapPM<Integer, MyModel> createMapProperty() {
        final MapPM<Integer, MyModel> map = new MapPM<Integer, MyModel>();
        for (int i = 0; i < NUM; ++i) {
            MyModel pModel = new MyModel();
            map.put(i, pModel);
        }
        return map;
    }

    @Test
    public void testClearSelection() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("selModel.getMaxSelectionIndex()", -1, selModel.getMaxSelectionIndex());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMaxSelectionIndex()", this.mapProperty.size() - 1, selModel.getMaxSelectionIndex());
        selModel.clearSelection();
        assertEquals("selModel.getMaxSelectionIndex()", -1, selModel.getMaxSelectionIndex());
    }

    @Test
    public void testSetSelectionInterval() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        selModel.setSelectionInterval(0, 1);
        assertEquals("mapProperty.getSelection().size()", 2, this.mapProperty.getSelection().size());
    }

    @Test
    public void testRemoveSelectionInterval() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        selModel.removeSelectionInterval(1, 1);
        assertEquals("mapProperty.getSelection().size()", NUM - 1, this.mapProperty.getSelection().size());
    }

    @Test
    public void testRemoveFromMapProperty() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        int c = 0;
        HashSet<Integer> keysToRemove = new HashSet<Integer>();
        for (Iterator<Integer> iterator = mapProperty.keyiterator(); iterator.hasNext(); c++) {
            Integer key = iterator.next();
            if (key == 0)
                continue;
            keysToRemove.add(key);
        }
        mapProperty.removeAllKeys(keysToRemove);
        assertEquals("mapProperty.size()", 1, mapProperty.size());
        assertEquals("mapProperty.getSelection().size()", 1, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
        assertEquals("selModel.getMaxSelectionIndex()", 0, selModel.getMaxSelectionIndex());
    }

    @Test
    public void testRemoveFromMapProperty2() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        int c = 0;
        mapProperty.removeAllIndices(new int[] { 0, 1, 6, 7, 8, 9 });

        assertEquals("mapProperty.size()", 4, mapProperty.size());
        assertEquals("mapProperty.getSelection().size()", 4, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
        assertEquals("selModel.getMaxSelectionIndex()", 3, selModel.getMaxSelectionIndex());
    }

    @Test
    public void testRemoveFromMapProperty3() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        int c = 0;
        mapProperty.removeAllIndices(new int[] { 5 });

        assertEquals("mapProperty.size()", 9, mapProperty.size());
        assertEquals("mapProperty.getSelection().size()", 9, this.mapProperty.getSelection().size());

        assertEquals("selModel.getMaxSelectionIndex()", 8, selModel.getMaxSelectionIndex());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
    }

    @Test
    public void testRemoveFromMapProperty4() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        int c = 0;
        mapProperty.removeAllIndices(new int[] { 9 });

        assertEquals("mapProperty.size()", 9, mapProperty.size());
        assertEquals("mapProperty.getSelection().size()", 9, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
        assertEquals("selModel.getMaxSelectionIndex()", 8, selModel.getMaxSelectionIndex());
    }

    @Test
    public void testRemoveFromMapProperty5() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        int c = 0;
        mapProperty.removeAllIndices(new int[] { 0 });

        assertEquals("mapProperty.size()", 9, mapProperty.size());
        assertEquals("mapProperty.getSelection().size()", 9, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
        assertEquals("selModel.getMaxSelectionIndex()", 8, selModel.getMaxSelectionIndex());
    }

    @Test
    public void testRemoveFromMapProperty6() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("this.mapProperty.getSelection().size()", 0, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        int c = 0;

        EventCounter counter = new EventCounter();
        mapProperty.addListListener(counter);
        mapProperty.getSelection().removeInterval(0, 0);
        mapProperty.removeAt(0);

        assertEquals("counter.elementsDeselectedEvents", 1, counter.elementsDeselectedEvents);
        assertEquals("counter.elementsRemovedEvents", 1, counter.elementsRemovedEvents);
        ElementsRemovedEvent evt = counter.getEventsOfType(ElementsRemovedEvent.class).get(0);
        assertEquals("evt.getBeginIndex()", 0, evt.getBeginIndex());
        assertEquals("evt.getBeginIndex()", 1, evt.getLength());

        assertEquals("mapProperty.size()", 9, mapProperty.size());
        assertEquals("mapProperty.getSelection().size()", 9, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
        assertEquals("selModel.getMaxSelectionIndex()", 8, selModel.getMaxSelectionIndex());
    }

    @Test
    public void testSetLeadSelectionIndex() {
        //TODO implement test
    }

    @Test
    public void testListPropertySelectionModel() {
        try {
            new BnTableSelectionModel(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }

    @Test
    public void testAddSelection() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("selModel.getMaxSelectionIndex()", -1, selModel.getMaxSelectionIndex());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMaxSelectionIndex()", NUM - 1, selModel.getMaxSelectionIndex());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
    }

    @Test
    public void removeSelection() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("selModel.getMaxSelectionIndex()", -1, selModel.getMaxSelectionIndex());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        this.mapProperty.getSelection().removeInterval(0, 0);
        assertEquals("this.mapProperty.getSelection().size()", NUM - 1, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMaxSelectionIndex()", NUM - 1, selModel.getMaxSelectionIndex());
        assertEquals("selModel.getMinSelectionIndex()", 1, selModel.getMinSelectionIndex());
    }

    @Test
    public void removeElements() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        assertEquals("selModel.getMaxSelectionIndex()", -1, selModel.getMaxSelectionIndex());
        this.mapProperty.getSelection().addAll();
        assertEquals("this.mapProperty.getSelection().size()", NUM, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMaxSelectionIndex()", NUM - 1, selModel.getMaxSelectionIndex());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
        this.mapProperty.removeAt(1);
        assertEquals("this.mapProperty.size()", NUM - 1, this.mapProperty.size());
        assertEquals("mapProperty.getSelection().size()", NUM - 1, this.mapProperty.getSelection().size());
        assertEquals("selModel.getMaxSelectionIndex()", NUM - 2, selModel.getMaxSelectionIndex());
        assertEquals("selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
    }

    @Test
    public void selectAll() {
        final BnTableSelectionModel selModel = new BnTableSelectionModel(this.mapProperty);
        mapProperty.getSelection().addAll();
        for (int i = 0; i < NUM; ++i) {
            assertEquals("selModel.isSelectedIndex(i='" + i + "')", true, selModel.isSelectedIndex(i));
        }
    }

    private static class EventCounter implements ListListener {
        int elementChangedEvents;
        int elementReplacedEvents;
        int elementsAddedEvents;
        int elementsDeselectedEvents;
        int elementsRemovedEvents;
        int elementsSelectedEvents;
        List<ListEvent> events = new LinkedList<ListEvent>();

        public void elementChanged(ElementChangedEvent evt) {
            events.add(evt);
            elementChangedEvents++;
        }

        public void elementsReplaced(ElementsReplacedEvent evt) {
            events.add(evt);
            elementReplacedEvents++;
        }

        public void elementsAdded(ElementsAddedEvent evt) {
            events.add(evt);
            elementsAddedEvents++;
        }

        public void elementsDeselected(ElementsDeselectedEvent evt) {
            events.add(evt);
            elementsDeselectedEvents++;
        }

        public void elementsRemoved(ElementsRemovedEvent evt) {
            events.add(evt);
            elementsRemovedEvents++;
        }

        public void elementsSelected(ElementsSelectedEvent evt) {
            events.add(evt);
            elementsSelectedEvents++;
        }

        public <T extends ListEvent> List<T> getEventsOfType(Class<T> type) {
            List<T> result = new LinkedList<T>();
            for (ListEvent evt : events) {
                if (type.isInstance(evt)) {
                    result.add((T)evt);
                }
            }
            return result;
        }
    }

    private static class MyModel extends AbstractPM {
        protected final TextPM text = new TextPM();

        public MyModel() {
            PMManager.setup(this);
        }
    }
}