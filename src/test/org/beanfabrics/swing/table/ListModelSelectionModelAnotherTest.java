/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ListSelectionModel;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.ModelProvider;
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
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Before;
import org.junit.Test;

public class ListModelSelectionModelAnotherTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ListModelSelectionModelAnotherTest.class);
    }

    int NUM = 10;
    int selstart = 2;
    int selend = 6;
    int sellen = selend - selstart + 1;

    public static class RowModel extends AbstractPM {
        protected final TextPM text = new TextPM();

        public RowModel() {
            PMManager.setup(this);
        }
    }

    ListPM<RowModel> listModel;
    BnColumn[] columns;
    List<BnColumn> defsList;
    EventCounter eventCounter;

    @Before
    public void setUp()
        throws Exception {
        listModel = new ListPM<RowModel>();
        for (int i = 0; i < NUM; ++i) {
            RowModel rowMdl = new RowModel();
            rowMdl.text.setText("" + i);
            listModel.add(rowMdl);
            if (selstart <= i && i <= selend) {
                listModel.getSelection().add(rowMdl);
            }
        }
        columns = new BnColumn[] { new BnColumn(new Path("text"), "Text") };
        defsList = Arrays.asList(columns);
        eventCounter = new EventCounter();
        listModel.addListListener(eventCounter);
    }

    private void moveSelectionDown() {
        int[] selectionIdxs = listModel.getSelection().getIndexes();
        Arrays.sort(selectionIdxs);
        for (int i = selectionIdxs.length - 1; i >= 0; i--) {
            listModel.swap(selectionIdxs[i], selectionIdxs[i] + 1);
        }
    }

    public void moveSelectionUp() {
        int[] selectionIdxs = listModel.getSelection().getIndexes();
        Arrays.sort(selectionIdxs);
        for (int i = 0; i < selectionIdxs.length; i++) {
            listModel.swap(selectionIdxs[i] - 1, selectionIdxs[i]);
        }

    }

    @Test
    public void moveUpWithSelectionModel() {
        doAssertionsBeforeTest();
        BnTableSelectionModel selModel = new BnTableSelectionModel(listModel);
        moveSelectionUp();
        doAssertionsAfterMoveUp(selModel);
    }

    @Test
    public void moveUpWithSelectionModelAndTableModel() {
        doAssertionsBeforeTest();
        new BnTableModel(listModel, defsList, true);
        BnTableSelectionModel selModel = new BnTableSelectionModel(listModel);
        moveSelectionUp();
        doAssertionsAfterMoveUp(selModel);
    }

    @Test
    public void moveUpWithTable()
        throws Exception {
        final BnTable bnTable = new BnTable();
        //		EventQueue.invokeLater(
        //		new Runnable() {
        //			public void run() {
        //				JFrame f = new JFrame();
        //				f.getContentPane().add(bnTable);
        //				f.setBounds(100, 100, 200, 400);
        //				f.setVisible(true);
        //			}
        //		});

        ModelProvider provider = new ModelProvider();
        bnTable.setModelProvider(provider);
        bnTable.setPath(new Path("this"));
        bnTable.setColumns(columns);
        provider.setPresentationModel(listModel);
        doAssertionsBeforeTest();

        //		Thread.sleep(2000);
        moveSelectionUp();
        //		Thread.sleep(2000);

        doAssertionsAfterMoveUp(bnTable.getSelectionModel());
        //		Thread.sleep(2000);
    }

    @Test
    public void moveDownWithSelectionModel() {
        doAssertionsBeforeTest();
        BnTableSelectionModel selModel = new BnTableSelectionModel(listModel);
        moveSelectionDown();
        doAssertionsAfterMoveDown(selModel);
    }

    @Test
    public void moveDownWithSelectionModelAndTableModel() {
        doAssertionsBeforeTest();
        new BnTableModel(listModel, defsList, true);
        BnTableSelectionModel selModel = new BnTableSelectionModel(listModel);
        moveSelectionDown();
        doAssertionsAfterMoveDown(selModel);
    }

    @Test
    public void moveDownWithTable()
        throws Exception {
        final BnTable bnTable = new BnTable();
        //		EventQueue.invokeLater(
        //		new Runnable() {
        //			public void run() {
        //				JFrame f = new JFrame();
        //				f.getContentPane().add(bnTable);
        //				f.setBounds(100, 100, 200, 400);
        //				f.setVisible(true);
        //			}
        //		});

        ModelProvider provider = new ModelProvider();
        bnTable.setModelProvider(provider);
        bnTable.setPath(new Path("this"));
        bnTable.setColumns(columns);
        provider.setPresentationModel(listModel);
        doAssertionsBeforeTest();

        //		Thread.sleep(2000);
        moveSelectionDown();

        doAssertionsAfterMoveDown(bnTable.getSelectionModel());
        //		Thread.sleep(2000);

        provider.setPresentationModel(null);
        provider.setPresentationModel(listModel);
        //		Thread.sleep(2000);

    }

    private void doAssertionsBeforeTest() {
        assertEquals("listCell.size()", NUM, listModel.size());
        assertEquals("listCell.getSelection().size()", sellen, listModel.getSelection().size());
        int cur = selstart;
        for (RowModel pModel : listModel.getSelection()) {
            assertEquals("pModel.text.getText()", "" + cur, pModel.text.getText());
            cur++;
        }

        int[] selectionIdxs = listModel.getSelection().getIndexes();
        int curIdx = selstart;
        for (int i = 0; i < selectionIdxs.length; ++i) {
            assertEquals("selectionIdxs[i='" + i + "']", curIdx, selectionIdxs[i]);
            curIdx++;
        }
    }

    private void doAssertionsAfterMoveDown(ListSelectionModel selModel) {
        assertEquals("listCell.size()", NUM, listModel.size());
        assertEquals("listCell.getSelection().size()", sellen, listModel.getSelection().size());
        int newSelstart = selstart + 1;
        //int newSelEnd = selend + 1;

        int[] selectionIdxs = listModel.getSelection().getIndexes();
        int curIdx = newSelstart;
        for (int i = 0; i < selectionIdxs.length; ++i) {
            assertEquals("selectionIdxs[i='" + i + "']", curIdx, selectionIdxs[i]);
            curIdx++;
        }

        int cur = selstart;
        for (RowModel pModel : listModel.getSelection()) {
            assertEquals("pModel.text.getText()", "" + cur, pModel.text.getText());
            cur++;
        }

        int numMoved = sellen * 2;
        assertEquals("eventCounter.elementChangedEvents", 0, eventCounter.elementChangedEvents);
        assertEquals("eventCounter.elementReplacedEvents", 0, eventCounter.elementReplacedEvents);
        assertEquals("eventCounter.elementsAddedEvents", numMoved, eventCounter.elementsAddedEvents);
        assertEquals("eventCounter.elementsRemovedEvents", numMoved, eventCounter.elementsRemovedEvents);
        assertEquals("eventCounter.elementsDeselectedEvents", sellen, eventCounter.elementsDeselectedEvents);
        assertEquals("eventCounter.elementsSelectedEvents", sellen, eventCounter.elementsSelectedEvents);
    }

    private void doAssertionsAfterMoveUp(ListSelectionModel selModel) {
        assertEquals("listCell.size()", NUM, listModel.size());
        assertEquals("listCell.getSelection().size()", sellen, listModel.getSelection().size());
        int newSelstart = selstart - 1;
        //int newSelEnd = selend - 1;

        int[] selectionIdxs = listModel.getSelection().getIndexes();
        int curIdx = newSelstart;
        for (int i = 0; i < selectionIdxs.length; ++i) {
            assertEquals("selectionIdxs[i='" + i + "']", curIdx, selectionIdxs[i]);
            curIdx++;
        }

        int cur = selstart;
        for (RowModel pModel : listModel.getSelection()) {
            assertEquals("pModel.text.getText()", "" + cur, pModel.text.getText());
            cur++;
        }

        //		assertEquals("eventCounter.elementChangedEvents", 0, eventCounter.elementChangedEvents);
        //		assertEquals("eventCounter.elementReplacedEvents", 0, eventCounter.elementReplacedEvents);
        //		assertEquals("eventCounter.elementsAddedEvents", 0, eventCounter.elementsAddedEvents);
        //		assertEquals("eventCounter.elementsDeselectedEvents", 2, eventCounter.elementsDeselectedEvents);
    }

    //		assertEquals("eventCounter.elementsRemovedEvents", 2, eventCounter.elementsRemovedEvents);
    //		assertEquals("eventCounter.elementsSelectedEvents", 0, eventCounter.elementsSelectedEvents);
    //		List<ElementsDeselectedEvent> deselectionEvents = eventCounter.getEventsOfType(ElementsDeselectedEvent.class);
    //		assertEquals("deselectionEvents.get(1).getBeginIndex()", 0, deselectionEvents.get(1).getBeginIndex());
    //		assertEquals("deselectionEvents.get(1).getLength()", 8, deselectionEvents.get(1).getLength());
    //		assertEquals("deselectionEvents.get(0).getBeginIndex()", 9, deselectionEvents.get(0).getBeginIndex());
    //		assertEquals("deselectionEvents.get(0).getLength()", 1, deselectionEvents.get(0).getLength());
    //		List<ElementsRemovedEvent> removedEvents = eventCounter.getEventsOfType(ElementsRemovedEvent.class);
    //		assertEquals("removedEvents.get(1).getBeginIndex()", 0, removedEvents.get(1).getBeginIndex());
    //		assertEquals("removedEvents.get(1).getLength()", 8, removedEvents.get(1).getLength());
    //		assertEquals("removedEvents.get(0).getBeginIndex()", 9, removedEvents.get(0).getBeginIndex());
    //		assertEquals("removedEvents.get(0).getLength()", 1, removedEvents.get(0).getLength());
    //		// check order of events
    //		{
    //			ListEvent[] expectedOrder = new ListEvent[] {
    //					deselectionEvents.get(0), removedEvents.get(0),
    //					deselectionEvents.get(1), removedEvents.get(1)
    //			};
    //			List<ListEvent> expectedOrderList = Arrays.asList(expectedOrder);
    //			List<ListEvent> actualOrderList = new ArrayList( eventCounter.events);
    //			actualOrderList.retainAll(expectedOrderList);
    //			ListEvent[] actualOrder = actualOrderList.toArray( new ListEvent[4]);
    //			assertArrayEquals("actualOrder", expectedOrder, actualOrder);
    //		}
    //
    //		assertEquals( "selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
    //		assertEquals( "selModel.getMaxSelectionIndex()", 0, selModel.getMaxSelectionIndex());
    //		assertEquals( "selModel.isSelectedIndex(0)", true, selModel.isSelectedIndex(0));
    //
    //	}

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
}