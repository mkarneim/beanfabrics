/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public abstract class IListPMAbstractInterfaceTest {
	static Class ELEMENT_TYPE;
	static int INITIAL_NUMBER_OF_ELEMENTS;
	static int[] INITIAL_SELECTED_ELEMENT_INDEXES; 
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		ELEMENT_TYPE = RowPM.class;
		INITIAL_NUMBER_OF_ELEMENTS = 10;
		INITIAL_SELECTED_ELEMENT_INDEXES = new int[] {0,1,2,3,4};
	}
	
	public static class RowPM extends AbstractPM {
		IntegerPM id = new IntegerPM();
		public RowPM() {
			PMManager.setup(this);
		}
	}
	
	IListPM<RowPM> list;
	List<RowPM> elements;
	
	@Before
	public void setUp() throws Exception {
		elements = new ArrayList<RowPM>(INITIAL_NUMBER_OF_ELEMENTS);
		for( int i=0; i<INITIAL_NUMBER_OF_ELEMENTS; ++i) {
			RowPM row = new RowPM();
			row.id.setInteger(i);
			elements.add(row);
		}
		this.list = create(elements, INITIAL_SELECTED_ELEMENT_INDEXES);
	}
	
	protected abstract IListPM<RowPM> create( Collection<RowPM> elements, int[] selectedIndexes) throws Exception;

	
	@Test
	public void testSetUp() {
		assertNotNull("elements", elements);
		assertNotNull("list", list);
		assertEquals("elements.size()", INITIAL_NUMBER_OF_ELEMENTS, elements.size());
		assertEquals("list.size()", INITIAL_NUMBER_OF_ELEMENTS, list.size());
		RowPM[] elementsArray = elements.toArray( new RowPM[elements.size()]);
		RowPM[] listElementsArray = list.toCollection().toArray( new RowPM[elements.size()]);
		assertArrayEquals("elementsArray", elementsArray, listElementsArray);
		assertEquals("list.getSelection().size()", INITIAL_SELECTED_ELEMENT_INDEXES.length, list.getSelection().size());
		for( int index: INITIAL_SELECTED_ELEMENT_INDEXES) {
			assertEquals("list.getSelection().contains(index='"+index+"')", true, list.getSelection().contains(index));
		}
	}

	@Test
	public void size() {
		assertEquals("list.size()", INITIAL_NUMBER_OF_ELEMENTS, list.size());
	}
	
	@Test
	public void isEmpty() {
		assertEquals("list.isEmpty()", INITIAL_NUMBER_OF_ELEMENTS==0, list.isEmpty());
	}
	
	@Test
	public void contains() {
		for( RowPM cell: elements) {
			assertEquals("list.contains(pM)", true, list.contains(cell));
		}
	}
	
	@Test
	public void indexOf() {
		for( int index=0; index<elements.size(); ++index) {
			RowPM cell = elements.get(index);
			assertEquals("list.indexOf(pM)", index, list.indexOf(cell));
		}
	}
	
	@Test
	public void getAt() {
		for( int index=0; index<elements.size(); ++index) {
			RowPM cell = elements.get(index);
			assertEquals("list.getAt(index='"+index+"')", cell, list.getAt(index));
		}
	}
	
	@Test
	public void toCollection() {
		Collection<RowPM> col = list.toCollection();
		assertNotNull("col", col);
		assertEquals("col.size()", elements.size(), col.size());
		Iterator<RowPM> it = col.iterator();
		for(int i=0; i<elements.size(); ++i) {
			assertEquals("[i='"+i+"'] it.hasNext()", true,it.hasNext());
			RowPM element = it.next();
			assertSame("[i='"+i+"'] element", elements.get(i), element);
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
		for(int i=0; i<elements.size(); ++i) {
			assertEquals("[i='"+i+"'] it.hasNext()", true,it.hasNext());
			RowPM element = it.next();
			assertSame("[i='"+i+"'] element", elements.get(i), element);
		}
	}
}