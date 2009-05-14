/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.model;

import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

/**
 * @author Michael Karneim
 */
public class ListPMInterfaceTest extends IListPMInterfaceAbstractTest {
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ListPMInterfaceTest.class);
    }
	
	protected IListPM<RowPM> create(Collection<RowPM> elements, int[] selectedIndexes) throws Exception {
		ListPM<RowPM> result = new ListPM<RowPM>();
		// add the initial elements
		for( RowPM cell: elements) {
			result.add(cell);
		}
		// create the inital selection
		for( int index: selectedIndexes) {
			result.getSelection().addInterval(index,index);
		}
		// assign the listCell
		return result;
	}
}