/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.table;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Before;
import org.junit.Test;

public class ListModelSelectionModelComplexTest {
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ListModelSelectionModelComplexTest.class);
    }
	private int NUM = 10;
	private OwnerModel ownerModel;
	private BnColumn[] columns;
	private List<BnColumn> defsList;
	private Set<Integer> keySetToRemove;
	private EventCounter eventCounter;

	@Before
	public void setUp() throws Exception {
		ownerModel = new OwnerModel();
		columns = new BnColumn[] {
				new BnColumn(new Path("text"),"Text")
		};
		defsList = Arrays.asList(columns);
		keySetToRemove = new HashSet<Integer>();
		for (int i = 0; i < NUM; ++i) {
			if ( i == 8) continue;
			keySetToRemove.add(i);
		}
		eventCounter = new EventCounter();
		ownerModel.elements.addListListener(eventCounter);
	}

	@Test
	public void testWithSelectionModelBefore() {
		BnTableSelectionModel selModel = new BnTableSelectionModel(ownerModel.elements);
		doAssertionsBeforeTest();
		ownerModel.elements.removeAllKeys(keySetToRemove);
		doAssertionsAfterTest( selModel);
	}

	@Test
	public void testWithSelectionModelAfter() {
		doAssertionsBeforeTest();
		ownerModel.elements.removeAllKeys(keySetToRemove);
		BnTableSelectionModel selModel = new BnTableSelectionModel(ownerModel.elements);
		doAssertionsAfterTest( selModel);
	}

	@Test
	public void testWithSelectionModelAndTableModel() {
//		BnTableModel tblModel = new BnTableModel( ownerModel.elements, defsList);
		BnTableSelectionModel selModel = new BnTableSelectionModel(ownerModel.elements);
		doAssertionsBeforeTest();
		ownerModel.elements.removeAllKeys(keySetToRemove);
		doAssertionsAfterTest( selModel);
	}

	@Test
	public void testWithTable() {
		BnTable bnTable = new BnTable();
		ModelProvider provider = new ModelProvider();
		bnTable.setModelProvider(provider);
		bnTable.setPath( new Path("elements"));
		bnTable.setColumns(columns);
		provider.setPresentationModel(ownerModel);
		doAssertionsBeforeTest();
		ownerModel.elements.removeAllKeys(keySetToRemove);
		doAssertionsAfterTest( bnTable.getSelectionModel());
	}

	private void doAssertionsBeforeTest() {
		assertEquals("ownerModel.elements.size()", NUM, ownerModel.elements.size());
		assertEquals("ownerModel.elements.getSelection().size()", NUM, ownerModel.elements.getSelection().size());
	}

	private void doAssertionsAfterTest(ListSelectionModel selModel) {
		assertEquals("ownerModel.elements.size()", 1, ownerModel.elements.size());
		assertEquals("ownerModel.elements.getSelection().size()", 1, ownerModel.elements.getSelection().size());
		assertEquals("ownerModel.elements.getSelectedKeys().contains(8)", true, ownerModel.elements.getSelectedKeys().contains(8));
		assertEquals("eventCounter.elementChangedEvents", 0, eventCounter.elementChangedEvents);
		assertEquals("eventCounter.elementReplacedEvents", 0, eventCounter.elementReplacedEvents);
		assertEquals("eventCounter.elementsAddedEvents", 0, eventCounter.elementsAddedEvents);
		assertEquals("eventCounter.elementsDeselectedEvents", 2, eventCounter.elementsDeselectedEvents);
		assertEquals("eventCounter.elementsRemovedEvents", 2, eventCounter.elementsRemovedEvents);
		assertEquals("eventCounter.elementsSelectedEvents", 0, eventCounter.elementsSelectedEvents);
		List<ElementsDeselectedEvent> deselectionEvents = eventCounter.getEventsOfType(ElementsDeselectedEvent.class);
		assertEquals("deselectionEvents.get(1).getBeginIndex()", 0, deselectionEvents.get(1).getBeginIndex());
		assertEquals("deselectionEvents.get(1).getLength()", 8, deselectionEvents.get(1).getLength());
		assertEquals("deselectionEvents.get(0).getBeginIndex()", 9, deselectionEvents.get(0).getBeginIndex());
		assertEquals("deselectionEvents.get(0).getLength()", 1, deselectionEvents.get(0).getLength());
		List<ElementsRemovedEvent> removedEvents = eventCounter.getEventsOfType(ElementsRemovedEvent.class);
		assertEquals("removedEvents.get(1).getBeginIndex()", 0, removedEvents.get(1).getBeginIndex());
		assertEquals("removedEvents.get(1).getLength()", 8, removedEvents.get(1).getLength());
		assertEquals("removedEvents.get(0).getBeginIndex()", 9, removedEvents.get(0).getBeginIndex());
		assertEquals("removedEvents.get(0).getLength()", 1, removedEvents.get(0).getLength());
		// check order of events
		{
			ListEvent[] expectedOrder = new ListEvent[] {
					deselectionEvents.get(0), removedEvents.get(0),
					deselectionEvents.get(1), removedEvents.get(1)
			};
			List<ListEvent> expectedOrderList = Arrays.asList(expectedOrder);
			List<ListEvent> actualOrderList = new ArrayList<ListEvent>(eventCounter.events);
			actualOrderList.retainAll(expectedOrderList);
			ListEvent[] actualOrder = actualOrderList.toArray( new ListEvent[4]);
			assertArrayEquals("actualOrder", expectedOrder, actualOrder);
		}

		assertEquals( "selModel.getMinSelectionIndex()", 0, selModel.getMinSelectionIndex());
		assertEquals( "selModel.getMaxSelectionIndex()", 0, selModel.getMaxSelectionIndex());
		assertEquals( "selModel.isSelectedIndex(0)", true, selModel.isSelectedIndex(0));

	}

	private static class EventCounter implements ListListener {
    	private int elementChangedEvents;
    	private int elementReplacedEvents;
    	private int elementsAddedEvents;
    	private int elementsDeselectedEvents;
    	private int elementsRemovedEvents;
    	private int elementsSelectedEvents;
    	private List<ListEvent> events = new LinkedList<ListEvent>();

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

		@SuppressWarnings("unchecked")
		public <T extends ListEvent> List<T> getEventsOfType( Class<T> type) {
			List<T> result = new LinkedList<T>();
			for( ListEvent evt: events) {
				if ( type.isInstance(evt)) {
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

	private class OwnerModel extends AbstractPM {
		MapPM<Integer, MyModel> elements = new MapPM<Integer, MyModel>( );
		public OwnerModel() {
			PMManager.setup(this);
			for (int i = 0; i < NUM; ++i) {
				elements.put(i, new MyModel());
			}
			elements.getSelection().addAll();
		}
	}
}